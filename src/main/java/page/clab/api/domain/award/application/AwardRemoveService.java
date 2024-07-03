package page.clab.api.domain.award.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.port.in.AwardRemoveUseCase;
import page.clab.api.domain.award.application.port.out.LoadAwardPort;
import page.clab.api.domain.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class AwardRemoveService implements AwardRemoveUseCase {

    private final LoadAwardPort loadAwardPort;
    private final RegisterAwardPort registerAwardPort;
    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;

    @Transactional
    @Override
    public Long remove(Long awardId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberInfoRetrievalUseCase.getCurrentMemberDetailedInfo();
        Award award = loadAwardPort.findByIdOrThrow(awardId);
        award.validateAccessPermission(currentMemberInfo);
        award.delete();
        return registerAwardPort.save(award).getId();
    }
}
