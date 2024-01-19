package page.clab.api.domain.book.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.domain.book.application.BookLoanRecordService;
import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.book.dto.response.BookLoanRecordResponseDto;
import page.clab.api.global.dto.PagedResponseDto;
import page.clab.api.global.dto.ResponseModel;

@RestController
@RequestMapping("/book-loan-records")
@RequiredArgsConstructor
@Tag(name = "BookLoanRecord", description = "도서 대출 관련 API")
@Slf4j
public class BookLoanRecordController {

    private final BookLoanRecordService bookLoanRecordService;

    @Operation(summary = "[U] 도서 대출", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/borrow")
    public ResponseModel borrowBook(
            @Valid @RequestBody BookLoanRecordRequestDto bookLoanRecordRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, CustomOptimisticLockingFailureException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = bookLoanRecordService.borrowBook(bookLoanRecordRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 도서 반납", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/return")
    public ResponseModel returnBook(
            @Valid @RequestBody BookLoanRecordRequestDto bookLoanRecordRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = bookLoanRecordService.returnBook(bookLoanRecordRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 도서 대출 연장", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/extend")
    public ResponseModel extendBookLoan(
            @Valid @RequestBody BookLoanRecordRequestDto bookLoanRecordRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = bookLoanRecordService.extendBookLoan(bookLoanRecordRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 도서 대출 내역 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getBookLoanRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BookLoanRecordResponseDto> bookLoanRecords = bookLoanRecordService.getBookLoanRecords(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(bookLoanRecords);
        return responseModel;
    }

    @Operation(summary = "[U] 도서 대출 내역 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "도서 ID, 대출자 ID를 기준으로 검색")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/search")
    public ResponseModel searchBookLoanRecord(
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) String borrowerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BookLoanRecordResponseDto> bookLoanRecords = bookLoanRecordService.searchBookLoanRecord(bookId, borrowerId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(bookLoanRecords);
        return responseModel;
    }

    @Operation(summary = "[U] 대출 상태의 도서 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/unreturned")
    public ResponseModel getUnreturnedBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BookLoanRecordResponseDto> unreturnedBooks = bookLoanRecordService.getUnreturnedBooks(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(unreturnedBooks);
        return responseModel;
    }

}
