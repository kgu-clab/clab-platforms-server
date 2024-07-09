package page.clab.api.domain.book.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookLoanRecordRequestDto {

    @NotNull(message = "{notNull.bookLoanRecord.bookId}")
    @Schema(description = "책 ID", example = "1", required = true)
    private Long bookId;
}
