package panntod.core.library.library_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panntod.core.library.library_system.dto.borrows.*;
import panntod.core.library.library_system.dto.commons.PageResponse;
import panntod.core.library.library_system.entities.Book;
import panntod.core.library.library_system.entities.Borrow;
import panntod.core.library.library_system.entities.BorrowItem;
import panntod.core.library.library_system.entities.User;
import panntod.core.library.library_system.enums.BorrowStatus;
import panntod.core.library.library_system.mappers.BorrowMapper;
import panntod.core.library.library_system.mappers.BorrowItemMapper;
import panntod.core.library.library_system.repositories.BookRepository;
import panntod.core.library.library_system.repositories.BorrowRepository;
import panntod.core.library.library_system.repositories.UserRepository;
import panntod.core.library.library_system.specs.BorrowSpecification;
import panntod.core.library.library_system.utils.PenaltyUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepo;
    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    private final BorrowMapper borrowMapper;
    private final BorrowItemMapper borrowItemMapper;

    public BorrowService(
            BorrowRepository borrowRepo,
            UserRepository userRepo,
            BookRepository bookRepo,
            BorrowMapper borrowMapper,
            BorrowItemMapper borrowItemMapper
    ) {
        this.borrowRepo = borrowRepo;
        this.userRepo = userRepo;
        this.bookRepo = bookRepo;
        this.borrowMapper = borrowMapper;
        this.borrowItemMapper = borrowItemMapper;
    }

    /**
     * CREATE Borrow transaction
     */
    @Transactional
    public BorrowDto create(BorrowCreateDto dto) {
        // Get Member
        User member = userRepo.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // Get Admin
        User admin = userRepo.findById(dto.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Map Borrow
        Borrow borrow = borrowMapper.toEntity(dto);
        borrow.setMember(member);
        borrow.setAdmin(admin);

        // Handle BorrowItems
        List<BorrowItem> items = dto.getItems().stream().map(itemDto -> {
            Book book = bookRepo.findById(itemDto.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found: " + itemDto.getBookId()));

            if (book.getStocks() < itemDto.getQty()) {
                throw new RuntimeException("Not enough stock for book: " + book.getName());
            }

            // Reduce stock
            book.setStocks(book.getStocks() - itemDto.getQty());
            bookRepo.save(book);

            BorrowItem borrowItem = borrowItemMapper.toEntity(itemDto);
            borrowItem.setBook(book);
            borrowItem.setBorrow(borrow);

            return borrowItem;
        }).toList();

        borrow.setItems(items);

        Borrow saved = borrowRepo.save(borrow);
        return borrowMapper.toDto(saved);
    }

    @Transactional
    public BorrowDto returnBorrow(UUID borrowId, UUID adminId) {
        Borrow borrow = borrowRepo.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Borrow transaction not found"));

        User admin = userRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Cek keterlambatan
        LocalDateTime now = LocalDateTime.now();
        boolean isLate = borrow.getDueDate() != null && now.isAfter(borrow.getDueDate());

        // Update stok buku
        for (BorrowItem item : borrow.getItems()) {
            Book book = item.getBook();
            book.setStocks(book.getStocks() + item.getQty());
            bookRepo.save(book);
        }

        borrow.setAdmin(admin);
        borrow.setReturnDate(now);

        if (isLate) {
            borrow.setPenalty(PenaltyUtil.calculatePenalty(borrow.getDueDate(), now));
            borrow.setStatus(BorrowStatus.LATE);
        } else {
            borrow.setStatus(BorrowStatus.RETURNED);
        }

        Borrow saved = borrowRepo.save(borrow);
        return borrowMapper.toDto(saved);
    }

    /**
     * SEARCH Borrow transactions
     */
    @Transactional(readOnly = true)
    public PageResponse<BorrowDto> search(BorrowSearchRequest req, Pageable pageable) {
        var spec = BorrowSpecification.bySearch(req);
        Page<Borrow> page = borrowRepo.findAll(spec, pageable);

        var content = page.stream()
                .map(borrowMapper::toDto)
                .toList();

        return new PageResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber() + 1,
                page.getSize()
        );
    }

    /**
     * FIND by ID
     */
    @Transactional(readOnly = true)
    public BorrowDto findById(UUID id) {
        return borrowRepo.findById(id)
                .map(borrowMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Borrow transaction not found"));
    }

    /**
     * DELETE Borrow (optional — soft delete bisa juga)
     */
    @Transactional
    public void delete(UUID id) {
        if (!borrowRepo.existsById(id)) {
            throw new RuntimeException("Borrow transaction not found");
        }
        borrowRepo.deleteById(id);
    }
}
