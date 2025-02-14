package page.clab.api.domain.library.bookLoanRecord.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.library.bookLoanRecord.application.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.library.bookLoanRecord.application.port.in.RequestBookLoanUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;

@RestController
@RequestMapping("/api/v1/book-loan-records")
@RequiredArgsConstructor
@Tag(name = "Library - Book Loan", description = "도서관 도서 대출")
public class BookLoanRequestController {

    private final RequestBookLoanUseCase requestBookLoanUseCase;

    @Operation(summary = "[U] 도서 대출 요청", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ApiResponse<Long> requestBookLoan(
        @Valid @RequestBody BookLoanRecordRequestDto requestDto
    ) throws CustomOptimisticLockingFailureException {
        Long id = requestBookLoanUseCase.requestBookLoan(requestDto);
        return ApiResponse.success(id);
    }
}
