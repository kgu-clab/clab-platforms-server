package page.clab.api.domain.book.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.book.application.BookService;
import page.clab.api.domain.book.dto.request.BookRequestDto;
import page.clab.api.domain.book.dto.request.BookUpdateRequestDto;
import page.clab.api.domain.book.dto.response.BookDetailsResponseDto;
import page.clab.api.domain.book.dto.response.BookResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "도서")
@Slf4j
public class BookController {

    private final BookService bookService;

    @Operation(summary = "[A] 도서 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createBook(
            @Valid @RequestBody BookRequestDto requestDto
    ) {
        Long id = bookService.createBook(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 도서 목록 조회(제목, 카테고리, 출판사, 대여자 ID, 대여자 이름 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "5개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "제목, 카테고리, 출판사, 대여자 ID, 대여자 이름 중 하나라도 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<BookResponseDto>> getBooksByConditions(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "publisher", required = false) String publisher,
            @RequestParam(name = "borrowerId", required = false) String borrowerId,
            @RequestParam(name = "borrowerName", required = false) String borrowerName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BookResponseDto> books = bookService.getBooksByConditions(title, category, publisher, borrowerId, borrowerName, pageable);
        return ApiResponse.success(books);
    }

    @Operation(summary = "[U] 도서 상세 정보", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{bookId}")
    public ApiResponse<BookDetailsResponseDto> getBook(
            @PathVariable(name = "bookId") Long bookId
    ) {
        BookDetailsResponseDto book = bookService.getBookDetails(bookId);
        return ApiResponse.success(book);
    }

    @Operation(summary = "[A] 도서 정보 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("")
    public ApiResponse<Long> updateBookInfo(
            @RequestParam(name = "bookId") Long bookId,
            @Valid @RequestBody BookUpdateRequestDto requestDto
    ) {
        Long id = bookService.updateBookInfo(bookId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[A] 도서 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{bookId}")
    public ApiResponse<Long> deleteBook(
            @PathVariable(name = "bookId") Long bookId
    ) {
        Long id = bookService.deleteBook(bookId);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 도서 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<BookDetailsResponseDto>> getDeletedBooks(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BookDetailsResponseDto> books = bookService.getDeletedBooks(pageable);
        return ApiResponse.success(books);
    }

}
