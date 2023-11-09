package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class BlogRequestDto {

    @NotNull(message = "{notNull.blog.title}")
    @Size(min = 1, max = 255, message = "{size.blog.title}")
    private String title;

    @NotNull(message = "{notNull.blog.subTitle}")
    @Size(min = 1, max = 255, message = "{size.blog.subTitle}")
    private String subTitle;

    @NotNull(message = "{notNull.blog.content}")
    @Size(min = 1, message = "{size.blog.content}")
    private String content;

    @URL(message = "{url.blog.imageUrl}")
    private String imageUrl;

    private String tag;

}
