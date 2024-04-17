package page.clab.api.domain.book.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.book.application.BookLoanRecordService;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.book.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.domain.book.dto.response.BookLoanRecordResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;

@RestController
@RequestMapping("/api/v1/book-loan-records")
@RequiredArgsConstructor
@Tag(name = "BookLoanRecord", description = "도서 대출")
@Slf4j
public class BookLoanRecordController {

    private final BookLoanRecordService bookLoanRecordService;

    @Operation(summary = "[U] 도서 대출 요청", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> requestBookLoan(
            @Valid @RequestBody BookLoanRecordRequestDto requestDto
    ) throws CustomOptimisticLockingFailureException {
        Long id = bookLoanRecordService.requestBookLoan(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 도서 반납", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/return")
    public ApiResponse<Long> returnBook(
            @Valid @RequestBody BookLoanRecordRequestDto requestDto
    ) {
        Long id = bookLoanRecordService.returnBook(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 도서 대출 연장", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/extend")
    public ApiResponse<Long> extendBookLoan(
            @Valid @RequestBody BookLoanRecordRequestDto requestDto
    ) {
        Long id = bookLoanRecordService.extendBookLoan(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[A] 도서 대출 승인", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/approve/{bookLoanRecordId}")
    public ApiResponse<Long> approveBookLoan(
            @PathVariable(name = "bookLoanRecordId") Long bookLoanRecordId
    ) {
        Long id = bookLoanRecordService.approveBookLoan(bookLoanRecordId);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[A] 도서 대출 거절", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/reject/{bookLoanRecordId}")
    public ApiResponse<Long> rejectBookLoan(
            @PathVariable(name = "bookLoanRecordId") Long bookLoanRecordId
    ) {
        Long id = bookLoanRecordService.rejectBookLoan(bookLoanRecordId);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 도서 대출 내역 조회(도서 ID, 대출자 ID, 대출 상태 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "도서 ID, 대출자 ID, 대출 가능 여부 중 하나라도 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/conditions")
    public ApiResponse<PagedResponseDto<BookLoanRecordResponseDto>> getBookLoanRecordsByConditions(
            @RequestParam(name = "bookId", required = false) Long bookId,
            @RequestParam(name = "borrowerId", required = false) String borrowerId,
            @RequestParam(name = "status", required = false) BookLoanStatus status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BookLoanRecordResponseDto> bookLoanRecords = bookLoanRecordService.getBookLoanRecordsByConditions(bookId, borrowerId, status, pageable);
        return ApiResponse.success(bookLoanRecords);
    }

    @Operation(summary = "[A] 도서 연체자 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/overdue")
    public ApiResponse<PagedResponseDto<BookLoanRecordOverdueResponseDto>> getOverdueBookLoanRecords(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BookLoanRecordOverdueResponseDto> overdueRecords = bookLoanRecordService.getOverdueBookLoanRecords(pageable);
        return ApiResponse.success(overdueRecords);
    }

}
