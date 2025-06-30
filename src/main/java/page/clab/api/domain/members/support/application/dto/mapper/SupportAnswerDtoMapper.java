package page.clab.api.domain.members.support.application.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.support.application.dto.request.SupportAnswerRequestDto;
import page.clab.api.domain.members.support.application.dto.response.SupportAnswerResponseDto;
import page.clab.api.domain.members.support.domain.SupportAnswer;

@Component
@RequiredArgsConstructor
public class SupportAnswerDtoMapper {

    public SupportAnswer fromDto(MemberBasicInfoDto member, Long supportId, SupportAnswerRequestDto requestDto) {
        return SupportAnswer.builder()
            .adminId(member.getMemberId())
            .adminName(member.getMemberName())
            .supportId(supportId)
            .content(requestDto.getContent())
            .isDeleted(false)
            .build();
    }

    public SupportAnswerResponseDto from(SupportAnswer supportAnswer) {
        return SupportAnswerResponseDto.builder()
            .content(supportAnswer.getContent())
            .responder(supportAnswer.getAdminName())
            .createdAt(supportAnswer.getCreatedAt())
            .build();
    }
}
