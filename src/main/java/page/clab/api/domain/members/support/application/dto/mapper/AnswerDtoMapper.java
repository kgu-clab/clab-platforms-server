package page.clab.api.domain.members.support.application.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.support.application.dto.request.AnswerRequestDto;
import page.clab.api.domain.members.support.domain.Answer;

@Component
@RequiredArgsConstructor
public class AnswerDtoMapper {

    public Answer fromDto(MemberBasicInfoDto member, Long supportId, AnswerRequestDto requestDto) {
        return Answer.builder()
                .adminId(member.getMemberId())
                .adminName(member.getMemberName())
                .supportId(supportId)
                .content(requestDto.getContent())
                .isDeleted(false)
                .build();
    }
}
