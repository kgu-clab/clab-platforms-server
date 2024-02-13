package page.clab.api.domain.activityPhoto.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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
public class ActivityPhotoRequestDto {

    @NotNull(message = "{notNull.activityPhoto.title}")
    @Schema(description = "활동 사진 제목", example = "2021년 1월 1일 활동 사진", required = true)
    private String title;

    @NotNull(message = "{notNull.activityPhoto.imageUrl}")
    @URL(message = "{url.activityPhoto.imageUrl}")
    @Schema(description = "활동 사진 URL", example = "https://static.gwansik.dev/clab.page/1.webp", required = true)
    private String imageUrl;

    @NotNull(message = "{notNull.activityPhoto.date}")
    @Schema(description = "활동 날짜", example = "2021-01-01", required = true)
    private LocalDate date;

}
