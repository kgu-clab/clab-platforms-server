package page.clab.api.type.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Blog;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogResponseDto {

    private Long id;

    private String memberId;

    private String name;

    private String imgageUrl;

    private String title;

    private String subTitle;

    private String content;

    private String imageUrl;

    private String tag;

    private LocalDateTime updateTime;

    private LocalDateTime createdAt;

    public static BlogResponseDto of(Blog blog) {
        BlogResponseDto blogResponseDto = ModelMapperUtil.getModelMapper().map(blog, BlogResponseDto.class);
        blogResponseDto.setMemberId(blog.getMember().getId());
        blogResponseDto.setName(blog.getMember().getName());
        blogResponseDto.setImgageUrl(blog.getMember().getImageUrl());
        return blogResponseDto;
    }

}
