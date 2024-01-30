package page.clab.api.domain.activityPhoto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityPhotoResponseDto {

    private Long id;

    private String imageUrl;

    private Boolean isPublic;

    public static ActivityPhotoResponseDto of(ActivityPhoto activityPhoto) {
        return ModelMapperUtil.getModelMapper().map(activityPhoto, ActivityPhotoResponseDto.class);
    }

}
