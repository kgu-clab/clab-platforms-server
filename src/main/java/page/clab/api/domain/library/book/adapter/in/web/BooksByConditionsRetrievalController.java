package page.clab.api.domain.library.book.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.library.book.application.dto.response.BookResponseDto;
import page.clab.api.domain.library.book.application.port.in.RetrieveBooksByConditionsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Library - Book", description = "도서관 도서")
public class BooksByConditionsRetrievalController {

    private final RetrieveBooksByConditionsUseCase retrieveBooksByConditionsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[G] 도서 목록 조회(제목, 카테고리, 출판사, 대여자 ID, 대여자 이름 기준)", description = "ROLE_GUEST 이상의 권한이 필요함<br>" +
        "5개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
        "제목, 카테고리, 출판사, 대여자 ID, 대여자 이름 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<BookResponseDto>> retrieveBooksByConditions(
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
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, BookResponseDto.class);
        PagedResponseDto<BookResponseDto> books = retrieveBooksByConditionsUseCase.retrieveBooks(title, category,
            publisher, borrowerId, borrowerName, pageable);
        return ApiResponse.success(books);
    }
}
