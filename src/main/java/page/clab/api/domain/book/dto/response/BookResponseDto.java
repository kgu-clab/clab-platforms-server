package page.clab.api.domain.book.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponseDto {

    private Long id;

    private String borrowerId;

    private String borrowerName;

    private String category;

    private String title;

    private String author;

    private String publisher;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updateTime;

    public static BookResponseDto of(Book book) {
        BookResponseDto bookResponseDto = ModelMapperUtil.getModelMapper().map(book, BookResponseDto.class);
        if (book.getBorrower() != null) {
            bookResponseDto.setBorrowerId(book.getBorrower().getId());
            bookResponseDto.setBorrowerName(book.getBorrower().getName());
        }
        return bookResponseDto;
    }

}
