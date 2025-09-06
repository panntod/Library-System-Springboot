package panntod.core.library.library_system.services;

import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panntod.core.library.library_system.dto.commons.LoginResponse;
import panntod.core.library.library_system.dto.commons.PageResponse;
import panntod.core.library.library_system.dto.users.*;
import panntod.core.library.library_system.entities.User;
import panntod.core.library.library_system.enums.TokenType;
import panntod.core.library.library_system.enums.UserRole;
import panntod.core.library.library_system.mappers.UserMapper;
import panntod.core.library.library_system.repositories.UserRepository;
import panntod.core.library.library_system.specs.UserSpecification;
import panntod.core.library.library_system.utils.JwtUtil;
import panntod.core.library.library_system.utils.PasswordUtil;
import panntod.core.library.library_system.utils.UserRoleUtil;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    public UserService(UserRepository repo, UserMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    /**
     * SEARCH users (default hanya active).
     * Jika showSoftDelete = true maka hanya SUPER_ADMIN yang boleh.
     */
    @Transactional(readOnly = true)
    public PageResponse<UserDto> search(UserSearchRequest req, Pageable pageable, boolean showSoftDelete) {
        if (showSoftDelete) {
            if (UserRoleUtil.getCurrentUserRole() != UserRole.SUPER_ADMIN) {
                throw new AccessDeniedException("Only SUPER_ADMIN can view soft deleted users");
            }
        } else {
            req.setIsActive(true);
        }

        var spec = UserSpecification.bySearch(req);
        Page<User> page = repo.findAll(spec, pageable);

        var content = page.stream().map(mapper::toDto).toList();

        return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages(), page.getNumber() + 1, page.getSize());
    }

    /**
     * CREATE user with hashed password
     */
    @Transactional
    public UserDto create(UserCreateDto createDto) {
        User user = mapper.toEntity(createDto);
        user.setPassword(PasswordUtil.hashPassword(createDto.getPassword())); // hash password
        User savedUser = repo.save(user);
        return mapper.toDto(savedUser);
    }

    /**
     * FIND by ID
     */
    @Transactional(readOnly = true)
    public UserDto findById(UUID id) {
        return repo.findById(id).map(mapper::toDto).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * FULL UPDATE (PUT)
     */
    @Transactional
    public UserDto update(UUID id, UserCreateDto updateDto) {
        User existing = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        // Update field langsung
        existing.setFirstName(updateDto.getFirstName());
        existing.setLastName(updateDto.getLastName());
        existing.setEmail(updateDto.getEmail());
        existing.setAddress(updateDto.getAddress());
        existing.setPhoneNumber(updateDto.getPhoneNumber());

        // Only Super Admin
        if (updateDto.getIsActive() != null) existing.setIsActive(updateDto.getIsActive());
        if (updateDto.getRole() != null) existing.setRole(updateDto.getRole());

        if (updateDto.getPassword() != null && !updateDto.getPassword().isBlank()) {
            existing.setPassword(PasswordUtil.hashPassword(updateDto.getPassword()));
        }

        String firstName = updateDto.getFirstName() != null ? updateDto.getFirstName() : existing.getFirstName();
        String lastName = updateDto.getLastName() != null ? updateDto.getLastName() : existing.getLastName();
        existing.setFullName((firstName + " " + lastName).trim());

        User saved = repo.save(existing);
        return mapper.toDto(saved);
    }

    @Transactional
    public UserDto patch(UUID id, UserPatchDto patchDto) {
        User existing = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (patchDto.firstName() != null) existing.setFirstName(patchDto.firstName());
        if (patchDto.lastName() != null) existing.setLastName(patchDto.lastName());
        if (patchDto.email() != null) existing.setEmail(patchDto.email());
        if (patchDto.address() != null) existing.setAddress(patchDto.address());
        if (patchDto.phoneNumber() != null) existing.setPhoneNumber(patchDto.phoneNumber());

        // Only Super Admin
        if (patchDto.isActive() != null) existing.setIsActive(patchDto.isActive());
        if (patchDto.role() != null) existing.setRole(patchDto.role());

        if (patchDto.password() != null && !patchDto.password().isBlank()) {
            existing.setPassword(PasswordUtil.hashPassword(patchDto.password()));
        }

        if (patchDto.firstName() != null || patchDto.lastName() != null) {
            String firstName = patchDto.firstName() != null ? patchDto.firstName() : existing.getFirstName();
            String lastName = patchDto.lastName() != null ? patchDto.lastName() : existing.getLastName();
            existing.setFullName((firstName + " " + lastName).trim());
        }

        User saved = repo.save(existing);
        return mapper.toDto(saved);
    }

    /**
     * SOFT DELETE (isActive = false)
     */
    @Transactional
    public void softDelete(UUID id) {
        User user = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsActive(false);
        repo.save(user);
    }

    /**
     * HARD DELETE (hanya SUPER_ADMIN)
     */
    @Transactional
    public void hardDelete(UUID id) {
        if (UserRoleUtil.getCurrentUserRole() != UserRole.SUPER_ADMIN) {
            throw new AccessDeniedException("Only SUPER_ADMIN can perform hard delete");
        }

        if (!repo.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(UserLoginDto loginRequest) {
        User user = repo.findByEmail(loginRequest.email().toLowerCase()).orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!PasswordUtil.checkPassword(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (user.getIsActive() != null && !user.getIsActive()) {
            throw new RuntimeException("User is inactive");
        }

        String accessToken = JwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = JwtUtil.generateRefreshToken(user.getId());

        return new LoginResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public String refreshAccessToken(String refreshToken) {
        UUID userId = JwtUtil.getUserIdFromToken(refreshToken, TokenType.REFRESH);
        User user = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return JwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
    }
}
