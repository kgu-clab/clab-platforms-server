package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class ActivityPhotoRequestDto {

    @NotNull(message = "{notNull.accuse.targetType}")
    @URL(message = "{url.activityPhoto.imageUrl}")
    @Schema(description = "활동 사진 URL", example = "https://static.gwansik.dev/clab.page/1.webp", required = true)
    private String imageUrl;

}
