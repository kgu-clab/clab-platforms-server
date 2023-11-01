package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.BookLoanRecord;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookLoanRecordResponseDto {

    private Long id;

    private Long bookId;

    private String borrowerId;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    public static BookLoanRecordResponseDto of(BookLoanRecord bookLoanRecord) {
        BookLoanRecordResponseDto bookLoanRecordResponseDto = ModelMapperUtil.getModelMapper()
                .map(bookLoanRecord, BookLoanRecordResponseDto.class);
        bookLoanRecordResponseDto.setBookId(bookLoanRecord.getBook().getId());
        bookLoanRecordResponseDto.setBorrowerId(bookLoanRecord.getBorrower().getId());
        return bookLoanRecordResponseDto;
    }

}
