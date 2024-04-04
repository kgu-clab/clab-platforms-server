package page.clab.api.domain.blog.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.blog.domain.Blog;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BlogDetailsResponseDto {

    private Long id;

    private String memberId;

    private String name;

    private String title;

    private String subTitle;

    private String content;

    private String imageUrl;

    @JsonProperty("isOwner")
    private Boolean isOwner;

    private LocalDateTime createdAt;

    public static BlogDetailsResponseDto toDto(Blog blog, boolean isOwner) {
        return BlogDetailsResponseDto.builder()
                .id(blog.getId())
                .memberId(blog.getMember().getId())
                .name(blog.getMember().getName())
                .title(blog.getTitle())
                .subTitle(blog.getSubTitle())
                .content(blog.getContent())
                .imageUrl(blog.getImageUrl())
                .isOwner(isOwner)
                .createdAt(blog.getCreatedAt())
                .build();
    }

}
