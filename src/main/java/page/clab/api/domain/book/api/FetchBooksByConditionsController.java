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
import page.clab.api.domain.book.application.FetchBooksByConditionsService;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.dto.response.BookResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "도서")
public class FetchBooksByConditionsController {

    private final FetchBooksByConditionsService fetchBooksByConditionsService;

    @Operation(summary = "[U] 도서 목록 조회(제목, 카테고리, 출판사, 대여자 ID, 대여자 이름 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "5개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "제목, 카테고리, 출판사, 대여자 ID, 대여자 이름 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, memberId")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<BookResponseDto>> getBooksByConditions(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "publisher", required = false) String publisher,
            @RequestParam(name = "borrowerId", required = false) String borrowerId,
            @RequestParam(name = "borrowerName", required = false) String borrowerName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, Book.class);
        PagedResponseDto<BookResponseDto> books = fetchBooksByConditionsService.execute(title, category, publisher, borrowerId, borrowerName, pageable);
        return ApiResponse.success(books);
    }
}
