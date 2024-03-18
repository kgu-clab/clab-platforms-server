package page.clab.api.domain.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookLoanRecordResponseDto {

    private Long bookId;

    private String borrowerId;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    private LocalDateTime dueDate;

    private Long loanExtensionCount;

    public static BookLoanRecordResponseDto of(BookLoanRecord bookLoanRecord) {
        BookLoanRecordResponseDto bookLoanRecordResponseDto = ModelMapperUtil.getModelMapper()
                .map(bookLoanRecord, BookLoanRecordResponseDto.class);
        bookLoanRecordResponseDto.setBookId(bookLoanRecord.getBook().getId());
        bookLoanRecordResponseDto.setBorrowerId(bookLoanRecord.getBorrower().getId());
        return bookLoanRecordResponseDto;
    }

}
