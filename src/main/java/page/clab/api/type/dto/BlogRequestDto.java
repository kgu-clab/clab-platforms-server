package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogRequestDto {

    @NotNull(message = "{notNull.blog.title}")
    @Size(min = 1, max = 255, message = "{size.blog.title}")
    @Schema(description = "제목", example = "Swagger Docs의 접근 권한을 제어하기 위한 여정", required = true)
    private String title;

    @NotNull(message = "{notNull.blog.subTitle}")
    @Size(min = 1, max = 255, message = "{size.blog.subTitle}")
    @Schema(description = "부제목", example = "Basic Auth와 JWT를 같이 사용하기 위한 Spring Security 디버깅", required = true)
    private String subTitle;

    @NotNull(message = "{notNull.blog.content}")
    @Size(min = 1, max = 10000, message = "{size.blog.content}")
    @Schema(description = "내용", example = "NestJs는 스웨거 설정에 있던데 스프링은........", required = true)
    private String content;

    @URL(message = "{url.blog.imageUrl}")
    @Schema(description = "이미지 URL", example = "https://www.clab.page/assets/logoWhite-fc1ef9a0.webp")
    private String imageUrl;

}
