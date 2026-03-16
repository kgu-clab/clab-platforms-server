package page.clab.api.domain.library.bookLoanRecord.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.library.bookLoanRecord.application.port.in.CancelBookLoanUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/book-loan-records")
@RequiredArgsConstructor
@Tag(name = "Library - Book Loan", description = "도서관 도서 대출")
public class BookLoanCancelController {

    private final CancelBookLoanUseCase cancelBookLoanUseCase;

    @Operation(summary = "[U] 도서 대출 요청 취소", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{bookLoanRecordId}")
    public ApiResponse<Long> cancelBookLoan(
        @PathVariable(name = "bookLoanRecordId") Long bookLoanRecordId
    ) {
        Long id = cancelBookLoanUseCase.cancelBookLoan(bookLoanRecordId);
        return ApiResponse.success(id);
    }
}
