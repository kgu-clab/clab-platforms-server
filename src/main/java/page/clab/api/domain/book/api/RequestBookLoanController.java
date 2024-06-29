package page.clab.api.domain.book.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.book.application.RequestBookLoanService;
import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;

@RestController
@RequestMapping("/api/v1/book-loan-records")
@RequiredArgsConstructor
@Tag(name = "BookLoanRecord", description = "도서 대출")
public class RequestBookLoanController {

     private final RequestBookLoanService requestBookLoanService;

    @Operation(summary = "[U] 도서 대출 요청", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> requestBookLoan(
            @Valid @RequestBody BookLoanRecordRequestDto requestDto
    ) throws CustomOptimisticLockingFailureException {
        Long id = requestBookLoanService.execute(requestDto);
        return ApiResponse.success(id);
    }
}