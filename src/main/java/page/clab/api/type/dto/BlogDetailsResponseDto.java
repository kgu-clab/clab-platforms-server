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
public class BlogDetailsResponseDto {

    private Long id;

    private String memberId;

    private String name;

    private String title;

    private String subTitle;

    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static BlogDetailsResponseDto of(Blog blog) {
        BlogDetailsResponseDto blogResponseDto = ModelMapperUtil.getModelMapper().map(blog, BlogDetailsResponseDto.class);
        blogResponseDto.setMemberId(blog.getMember().getId());
        blogResponseDto.setName(blog.getMember().getName());
        return blogResponseDto;
    }

}
