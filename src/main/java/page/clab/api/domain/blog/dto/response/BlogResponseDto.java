package page.clab.api.domain.blog.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.blog.domain.Blog;

import java.time.LocalDateTime;

@Getter
@Builder
public class BlogResponseDto {

    private Long id;

    private String title;

    private String subTitle;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static BlogResponseDto toDto(Blog blog) {
        return BlogResponseDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .subTitle(blog.getSubTitle())
                .imageUrl(blog.getImageUrl())
                .createdAt(blog.getCreatedAt())
                .build();
    }

}
