package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupDto {

    private String category;

    private String name;

    private String content;

//    private String imageUrl;

    public static ActivityGroupDto of(ActivityGroup activityGroup) {
        return ModelMapperUtil.getModelMapper().map(activityGroup, ActivityGroupDto.class);
    }

}
