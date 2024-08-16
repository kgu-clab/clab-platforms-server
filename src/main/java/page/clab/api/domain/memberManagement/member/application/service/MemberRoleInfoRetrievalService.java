package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberRoleInfoResponseDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberRoleInfoUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberRoleInfoRetrievalService implements RetrieveMemberRoleInfoUseCase {

    private final RetrieveMemberPort retrieveMemberPort;

    @Transactional(readOnly = true)
    @Override
    public List<MemberRoleInfoResponseDto> retrieveMemberRoleInfo(Pageable pageable) {
        List<Member> members = retrieveMemberPort.findAll(pageable);
        return MemberRoleInfoResponseDto.toDto(members);
    }
}
