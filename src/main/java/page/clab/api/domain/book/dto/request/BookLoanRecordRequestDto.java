package page.clab.api.domain.book.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookLoanRecordRequestDto {

    @NotNull(message = "{notNull.bookLoanRecord.bookId}")
    @Schema(description = "책 ID", example = "1", required = true)
    private Long bookId;

    @NotNull(message = "{notNull.bookLoanRecord.borrowerId}")
    @Schema(description = "대출자 ID", example = "202312000", required = true)
    private String borrowerId;

}
