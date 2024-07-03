package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMembersByConditionsUseCase;
import page.clab.api.domain.member.application.port.out.RetrieveMembersByConditionsPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MembersByConditionsRetrievalService implements RetrieveMembersByConditionsUseCase {

    private final RetrieveMembersByConditionsPort retrieveMembersByConditionsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MemberResponseDto> retrieve(String id, String name, Pageable pageable) {
        Page<Member> members = retrieveMembersByConditionsPort.findByConditions(id, name, pageable);
        return new PagedResponseDto<>(members.map(MemberResponseDto::toDto));
    }
}
