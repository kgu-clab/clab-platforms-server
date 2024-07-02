package page.clab.api.domain.award.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.AwardRemoveUseCase;
import page.clab.api.domain.award.dao.AwardRepository;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class AwardRemoveService implements AwardRemoveUseCase {

    private final AwardRepository awardRepository;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional
    @Override
    public Long remove(Long awardId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberDetailedInfo();
        Award award = getAwardByIdOrThrow(awardId);
        award.validateAccessPermission(currentMemberInfo);
        award.delete();
        awardRepository.save(award);
        return award.getId();
    }

    private Award getAwardByIdOrThrow(Long awardId) {
        return awardRepository.findById(awardId)
                .orElseThrow(() -> new NotFoundException("해당 수상 이력이 존재하지 않습니다."));
    }
}