package page.clab.api.domain.book.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.book.application.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.book.application.port.in.ExtendBookLoanUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/book-loan-records")
@RequiredArgsConstructor
@Tag(name = "BookLoanRecord", description = "도서 대출")
public class BookLoanExtensionController {

    private final ExtendBookLoanUseCase extendBookLoanUseCase;

    @Operation(summary = "[U] 도서 대출 연장", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @PostMapping("/extend")
    public ApiResponse<Long> extendBookLoan(
            @Valid @RequestBody BookLoanRecordRequestDto requestDto
    ) {
        Long id = extendBookLoanUseCase.extendBookLoan(requestDto);
        return ApiResponse.success(id);
    }
}
