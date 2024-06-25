package page.clab.api.domain.accuse.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.member.dto.shared.MemberInfoDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AccuseResponseDto {

    private List<MemberInfoDto> members;

    private TargetType targetType;

    private Long targetId;

    private String reason;

    private AccuseStatus accuseStatus;

    private Long accuseCount;

    private LocalDateTime createdAt;

    public static AccuseResponseDto toDto(Accuse accuse, List<MemberInfoDto> members) {
        return AccuseResponseDto.builder()
                .members(members)
                .targetType(accuse.getTarget().getTargetType())
                .targetId(accuse.getTarget().getTargetReferenceId())
                .reason(accuse.getReason())
                .accuseStatus(accuse.getTarget().getAccuseStatus())
                .accuseCount(accuse.getTarget().getAccuseCount())
                .createdAt(accuse.getCreatedAt())
                .build();
    }

}
