package page.clab.api.domain.book.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.book.application.port.in.ApproveBookLoanUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/book-loan-records")
@RequiredArgsConstructor
@Tag(name = "BookLoanRecord", description = "도서 대출")
public class BookLoanApprovalController {

    private final ApproveBookLoanUseCase approveBookLoanUseCase;

    @Operation(summary = "[A] 도서 대출 승인", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/approve/{bookLoanRecordId}")
    public ApiResponse<Long> approveBookLoan(
            @PathVariable(name = "bookLoanRecordId") Long bookLoanRecordId
    ) {
        Long id = approveBookLoanUseCase.approve(bookLoanRecordId);
        return ApiResponse.success(id);
    }
}