package page.clab.api.domain.book.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.book.application.port.in.RetrieveOverdueBookLoanRecordsUseCase;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book-loan-records")
@RequiredArgsConstructor
@Tag(name = "BookLoanRecord", description = "도서 대출")
public class OverdueBookLoanRecordsRetrievalController {

    private final RetrieveOverdueBookLoanRecordsUseCase retrieveOverdueBookLoanRecordsUseCase;

    @Operation(summary = "[A] 도서 연체자 조회", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, bookId, borrowedAt, dueDate, loanExtensionCount, returnedAt, memberId")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/overdue")
    public ApiResponse<PagedResponseDto<BookLoanRecordOverdueResponseDto>> retrieveOverdueBookLoanRecords(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "dueDate") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, BookLoanRecord.class);
        PagedResponseDto<BookLoanRecordOverdueResponseDto> overdueRecords =
                retrieveOverdueBookLoanRecordsUseCase.retrieveOverdueBookLoanRecords(pageable);
        return ApiResponse.success(overdueRecords);
    }
}
