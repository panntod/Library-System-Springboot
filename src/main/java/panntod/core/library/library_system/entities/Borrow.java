package panntod.core.library.library_system.entities;

import jakarta.persistence.*;
import lombok.Data;
import panntod.core.library.library_system.enums.BorrowStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(
        name = "borrows",
        indexes = {
                @Index(name = "idx_borrows_member_id", columnList = "member_id"),
                @Index(name = "idx_borrows_admin_id", columnList = "admin_id"),
                @Index(name = "idx_borrows_status", columnList = "status")
        }
)
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Member yang meminjam
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    // Admin yang melayani
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Column(nullable = false)
    private LocalDateTime borrowDate;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BorrowStatus status = BorrowStatus.BORROWED;

    // relasi ke detail item
    @OneToMany(mappedBy = "borrow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowItem> items;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column
    private Double penalty = 0.0;

    @PrePersist
    protected void onCreate() {
        if (this.borrowDate == null) {
            this.borrowDate = LocalDateTime.now();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
