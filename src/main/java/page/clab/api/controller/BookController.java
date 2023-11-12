package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.BookService;
import page.clab.api.type.dto.BookRequestDto;
import page.clab.api.type.dto.BookResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "도서 관련 API")
@Slf4j
public class BookController {

    private final BookService bookService;

    @Operation(summary = "[A] 도서 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createBook(
            @Valid @RequestBody BookRequestDto bookRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        bookService.createBook(bookRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 도서 목록", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getBooks() {
        List<BookResponseDto> books = bookService.getBooks();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(books);
        return responseModel;
    }

    @Operation(summary = "[U] 도서 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "카테고리, 제목, 저자, 출판사, 대여자를 기준으로 검색")
    @GetMapping("/search")
    public ResponseModel searchBook(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String borrowerId
    ) {
        List<BookResponseDto> books = bookService.searchBook(category, title, author, publisher, borrowerId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(books);
        return responseModel;
    }

    @Operation(summary = "[A] 도서 정보 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PatchMapping("")
    public ResponseModel updateBookInfo(
            @RequestParam Long bookId,
            @Valid @RequestBody BookRequestDto bookRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        bookService.updateBookInfo(bookId, bookRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 도서 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @DeleteMapping("/{bookId}")
    public ResponseModel deleteBook(
            @PathVariable("bookId") Long bookId
    ) throws PermissionDeniedException {
        bookService.deleteBook(bookId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
