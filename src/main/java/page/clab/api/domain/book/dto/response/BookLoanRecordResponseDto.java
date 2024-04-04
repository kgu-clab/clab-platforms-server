package page.clab.api.domain.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookLoanRecordResponseDto {

    private Long bookId;

    private String bookTitle;

    private String bookImageUrl;

    private String borrowerId;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    private LocalDateTime dueDate;

    private Long loanExtensionCount;

}
