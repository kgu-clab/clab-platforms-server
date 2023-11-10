package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequestDto {

    @NotNull(message = "{notNull.book.category}")
    private String category;

    @NotNull(message = "{notNull.book.title}")
    private String title;

    @NotNull(message = "{notNull.book.author}")
    private String author;

    @NotNull(message = "{notNull.book.publisher}")
    private String publisher;

    @URL(message = "{url.book.imageUrl}")
    private String imageUrl;

}
