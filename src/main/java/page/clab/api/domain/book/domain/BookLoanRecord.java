package page.clab.api.domain.book.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookLoanRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member borrower;

    @Column(updatable = false)
    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    private LocalDateTime dueDate;

    private Long loanExtensionCount;

    public static BookLoanRecord create(Book book, Member borrower) {
        return BookLoanRecord.builder()
                .book(book)
                .borrower(borrower)
                .borrowedAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusWeeks(1))
                .loanExtensionCount(0L)
                .build();
    }

    public void markAsReturned() {
        if (this.returnedAt != null) {
            throw new IllegalStateException("이미 반납된 도서입니다.");
        }
        this.returnedAt = LocalDateTime.now();
        if (isOverdue(returnedAt)) {
            long overdueDays = ChronoUnit.DAYS.between(this.dueDate, this.returnedAt);
            this.borrower.handleOverdueAndSuspension(overdueDays);
        }
    }

    private boolean isOverdue(LocalDateTime returnedAt) {
        return returnedAt.isAfter(this.dueDate);
    }

}
