package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Blog;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogResponseDto {

    private Long id;

    private String title;

    private String subTitle;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static BlogResponseDto of(Blog blog) {
        return ModelMapperUtil.getModelMapper().map(blog, BlogResponseDto.class);
    }

}
