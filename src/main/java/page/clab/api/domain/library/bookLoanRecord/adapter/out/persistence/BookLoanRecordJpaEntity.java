package page.clab.api.domain.library.bookLoanRecord.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Table(name = "book_loan_record")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE book_loan_record SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class BookLoanRecordJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    @Column(name = "member_id", nullable = false)
    private String borrowerId;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    private LocalDateTime dueDate;

    private Long loanExtensionCount;

    @Enumerated(EnumType.STRING)
    private BookLoanStatus status;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}
