package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Absent;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbsentRequestDto {

    @NotNull(message = "{notNull.absent.absenteeId}")
    @Schema(description = "불참 학생 학번", example = "202311220", required = true)
    private String absenteeId;

    @NotNull(message = "{notNull.absent.activityGroupId}")
    @Schema(description = "불참 그룹 아이디", example = "1", required = true)
    private Long activityGroupId;

    @NotNull(message = "{notNull.absent.reason}")
    @Schema(description = "불참 사유", example = "독감", required = true)
    private String reason;

    @NotNull(message = "{notNull.absent.absentDate}")
    @Schema(description = "불참 날짜", example = "2023-11-12", required = true)
    private LocalDate absentDate;

    public static AbsentRequestDto of(Absent absent){
        return ModelMapperUtil.getModelMapper().map(absent, AbsentRequestDto.class);
    }

}
