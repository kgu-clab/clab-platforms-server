package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Accuse;
import page.clab.api.type.etc.AccuseStatus;
import page.clab.api.type.etc.TargetType;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDateTime;

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
