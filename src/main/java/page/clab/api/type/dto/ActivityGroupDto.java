package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.util.ModelMapperUtil;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupDto {

    @NotNull(message = "{notnull.activityGroup.name}")
    @Size(min = 1, max = 30, message = "{size.activityGroup.name}")
    @Schema(description = "활동명", example = "2024-1 신입생 대상 C언어 스터디")
    private String name;

    @NotNull(message = "{notnull.activityGroup.content}")
    @Size(min = 1, max = 1000, message = "{size.activityGroup.content}")
    @Schema(description = "활동 설명", example = "2024-1 신입생 대상 C언어 스터디")
    private String content;

    @URL(message = "{url.activityGroup.imageUrl}")
    @Schema(description = "활동 이미지 URL", example = "https://i.namu.wiki/i/KcqDuQYTxNpUcLIMZTg28QXse0XiWx1G7K68kYYCo1GuhoHmhB_V8Qe9odGGt0BH9-0nQZTN53WXTNpDmwVfWQ.svg")
    private String imageUrl;

    public static ActivityGroupDto of(ActivityGroup activityGroup) {
        return ModelMapperUtil.getModelMapper().map(activityGroup, ActivityGroupDto.class);
    }

}
