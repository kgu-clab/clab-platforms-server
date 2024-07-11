package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.memberManagement.member.application.port.in.EnsureMemberExistenceUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.CheckMemberExistencePort;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class MemberExistenceService implements EnsureMemberExistenceUseCase {

    private final CheckMemberExistencePort checkMemberExistencePort;

    @Override
    public void ensureMemberExists(String memberId) {
        if (!checkMemberExistencePort.existsById(memberId)) {
            throw new NotFoundException("[Member] id: " + memberId + "에 해당하는 회원이 존재하지 않습니다.");
        }
    }
}
