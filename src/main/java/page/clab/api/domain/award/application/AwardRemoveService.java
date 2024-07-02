package page.clab.api.domain.award.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.port.in.AwardRemoveUseCase;
import page.clab.api.domain.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.award.application.port.out.RetrieveAwardByIdPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class AwardRemoveService implements AwardRemoveUseCase {

    private final RetrieveAwardByIdPort retrieveAwardByIdPort;
    private final RegisterAwardPort registerAwardPort;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional
    @Override
    public Long remove(Long awardId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberDetailedInfo();
        Award award = retrieveAwardByIdPort.findByIdOrThrow(awardId);
        award.validateAccessPermission(currentMemberInfo);
        award.delete();
        return registerAwardPort.save(award).getId();
    }
}
