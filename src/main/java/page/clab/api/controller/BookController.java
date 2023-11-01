package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book")
@Slf4j
public class BookController {

    private final BookService bookService;

    @Operation(summary = "도서 등록", description = "도서 등록<br>" +
            "String category;<br>" +
            "String title;<br>" +
            "String author;<br>" +
            "String publisher;<br>" +
            "String imageUrl;<br>")
    @PostMapping("")
    public ResponseModel createBook(
            @RequestBody BookRequestDto bookRequestDto
    ) throws PermissionDeniedException {
        bookService.createBook(bookRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "도서 목록", description = "도서 목록")
    @GetMapping("")
    public ResponseModel getBooks() {
        List<BookResponseDto> books = bookService.getBooks();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(books);
        return responseModel;
    }

    @Operation(summary = "도서 검색", description = "책 정보를 기반으로 검색")
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

    @Operation(summary = "도서 정보 수정", description = "도서 정보 수정<br>" +
            "String category;<br>" +
            "String title;<br>" +
            "String author;<br>" +
            "String publisher;<br>" +
            "String imageUrl;<br>")
    @PatchMapping("")
    public ResponseModel updateBookInfo(
            @RequestParam Long bookId,
            @RequestBody BookRequestDto bookRequestDto
    ) throws PermissionDeniedException {
        bookService.updateBookInfo(bookId, bookRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "도서 삭제", description = "도서 삭제")
    @DeleteMapping("/{bookId}")
    public ResponseModel deleteBook(
            @PathVariable("bookId") Long bookId
    ) throws PermissionDeniedException {
        bookService.deleteBook(bookId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
