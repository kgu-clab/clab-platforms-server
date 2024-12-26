package page.clab.api.domain.library.bookLoanRecord.application.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookLoanRecordOverdueResponseDto {

    private Long bookId;
    private String bookTitle;
    private String borrowerId;
    private String borrowerName;
    private LocalDateTime borrowedAt;
    private LocalDateTime dueDate;
    private BookLoanStatus status;
}
