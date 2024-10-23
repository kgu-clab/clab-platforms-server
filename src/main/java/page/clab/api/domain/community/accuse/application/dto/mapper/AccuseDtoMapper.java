package page.clab.api.domain.community.accuse.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.community.accuse.application.dto.request.AccuseRequestDto;
import page.clab.api.domain.community.accuse.application.dto.response.AccuseMyResponseDto;
import page.clab.api.domain.community.accuse.application.dto.response.AccuseResponseDto;
import page.clab.api.domain.community.accuse.domain.Accuse;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.AccuseTarget;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;

import java.util.List;

@Component
public class AccuseDtoMapper {

    public Accuse fromDto(AccuseRequestDto requestDto, String memberId, AccuseTarget target) {
        return Accuse.builder()
                .memberId(memberId)
                .target(target)
                .reason(requestDto.getReason())
                .isDeleted(false)
                .build();
    }

    public AccuseTarget fromDto(AccuseRequestDto requestDto) {
        return AccuseTarget.builder()
                .targetType(requestDto.getTargetType())
                .targetReferenceId(requestDto.getTargetId())
                .accuseCount(1L)
                .accuseStatus(AccuseStatus.PENDING)
                .build();
    }

    public AccuseMyResponseDto toDto(Accuse accuse) {
        return AccuseMyResponseDto.builder()
                .targetType(accuse.getTarget().getTargetType())
                .targetId(accuse.getTarget().getTargetReferenceId())
                .reason(accuse.getReason())
                .accuseStatus(accuse.getTarget().getAccuseStatus())
                .createdAt(accuse.getTarget().getCreatedAt())
                .build();
    }

    public AccuseResponseDto toDto(Accuse accuse, List<MemberBasicInfoDto> members) {
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
