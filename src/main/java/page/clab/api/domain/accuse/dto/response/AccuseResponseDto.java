package page.clab.api.domain.accuse.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccuseResponseDto {

    private String memberId;

    private String name;

    private TargetType targetType;

    private Long targetId;

    private String reason;

    private AccuseStatus accuseStatus;

    private LocalDateTime createdAt;

    public static AccuseResponseDto of(Accuse accuse) {
        AccuseResponseDto accuseResponseDto = ModelMapperUtil.getModelMapper().map(accuse, AccuseResponseDto.class);
        accuseResponseDto.setMemberId(accuse.getMember().getId());
        accuseResponseDto.setName(accuse.getMember().getName());
        return accuseResponseDto;
    }
}
