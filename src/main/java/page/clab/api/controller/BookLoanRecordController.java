package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.service.BookLoanRecordService;
import page.clab.api.type.dto.BookLoanRecordRequestDto;
import page.clab.api.type.dto.BookLoanRecordResponseDto;
import page.clab.api.type.dto.ResponseModel;

import java.util.List;

@RestController
@RequestMapping("/book-loan-records")
@RequiredArgsConstructor
@Tag(name = "BookLoanRecord")
@Slf4j
public class BookLoanRecordController {

    private final BookLoanRecordService bookLoanRecordService;

    @Operation(summary = "도서 대출", description = "도서 대출")
    @PostMapping("/borrow")
    public ResponseModel borrowBook(
            @RequestBody BookLoanRecordRequestDto bookLoanRecordRequestDto
    ) {
        bookLoanRecordService.borrowBook(bookLoanRecordRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "도서 반납", description = "도서 반납")
    @PostMapping("/return")
    public ResponseModel returnBook(
            @RequestBody BookLoanRecordRequestDto bookLoanRecordRequestDto
    ) {
        bookLoanRecordService.returnBook(bookLoanRecordRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "도서 대출 연장", description = "도서 대출 연장")
    @PostMapping("/extend")
    public ResponseModel extendBookLoan(
            @RequestBody BookLoanRecordRequestDto bookLoanRecordRequestDto
    ) {
        bookLoanRecordService.extendBookLoan(bookLoanRecordRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "도서 대출 내역 조회", description = "도서 대출 내역 조회")
    @GetMapping("")
    public ResponseModel getBookLoanRecords() {
        List<BookLoanRecordResponseDto> bookLoanRecords = bookLoanRecordService.getBookLoanRecords();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(bookLoanRecords);
        return responseModel;
    }

    @Operation(summary = "도서 대출 내역 검색", description = "도서 아이디와 멤버 아이디를 기반으로 검색")
    @GetMapping("/search")
    public ResponseModel searchBookLoanRecord(
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) String borrowerId
    ) {
        List<BookLoanRecordResponseDto> bookLoanRecords = bookLoanRecordService.searchBookLoanRecord(bookId, borrowerId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(bookLoanRecords);
        return responseModel;
    }

    @Operation(summary = "미반납 도서 조회", description = "미반납 도서 조회")
    @GetMapping("/unreturned")
    public ResponseModel getUnreturnedBooks() {
        List<BookLoanRecordResponseDto> unreturnedBooks = bookLoanRecordService.getUnreturnedBooks();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(unreturnedBooks);
        return responseModel;
    }

}
