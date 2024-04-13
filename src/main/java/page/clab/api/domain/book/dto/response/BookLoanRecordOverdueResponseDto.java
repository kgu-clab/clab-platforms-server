package page.clab.api.domain.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

}
