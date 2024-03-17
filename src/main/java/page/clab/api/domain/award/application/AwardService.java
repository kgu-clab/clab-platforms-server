package page.clab.api.domain.award.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.award.dao.AwardRepository;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.request.AwardRequestDto;
import page.clab.api.domain.award.dto.request.AwardUpdateRequestDto;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final MemberService memberService;

    private final AwardRepository awardRepository;

    public Long createAward(AwardRequestDto awardRequestDto) {
        Member member = memberService.getCurrentMember();
        Award award = Award.create(awardRequestDto, member);
        return awardRepository.save(award).getId();
    }

    @Transactional
    public PagedResponseDto<AwardResponseDto> getAwardsByConditions(String memberId, Long year, Pageable pageable) {
        Page<Award> awards = awardRepository.findByConditions(memberId, year, pageable);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::of));
    }

    public PagedResponseDto<AwardResponseDto> getMyAwards(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Award> awards = getAwardByMember(pageable, member);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::of));
    }

    public Long updateAward(Long awardId, AwardUpdateRequestDto awardUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Award award = getAwardByIdOrThrow(awardId);
        if (!isMemberHasAuthorityToManipulate(member, award)) {
            throw new PermissionDeniedException("해당 수상 이력을 수정할 권한이 없습니다.");
        }
        award.update(awardUpdateRequestDto);
        return awardRepository.save(award).getId();
    }

    public Long deleteAward(Long awardId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Award award = getAwardByIdOrThrow(awardId);
        if (!isMemberHasAuthorityToManipulate(member, award)) {
            throw new PermissionDeniedException("해당 수상 이력을 삭제할 권한이 없습니다.");
        }
        awardRepository.delete(award);
        return award.getId();
    }

    private boolean isMemberAwardee(Member member, Award award) {
        return award.getMember().getId().equals(member.getId());
    }

    private boolean isMemberHasAuthorityToManipulate(Member member, Award award) {
        return (isMemberAwardee(member, award) || memberService.isMemberSuperRole(member));
    }

    private Award getAwardByIdOrThrow(Long awardId) {
        return awardRepository.findById(awardId)
                .orElseThrow(() -> new NotFoundException("해당 수상 이력이 존재하지 않습니다."));
    }

    private Page<Award> getAwardByMember(Pageable pageable, Member member) {
        return awardRepository.findAllByMemberOrderByAwardDateDesc(member, pageable);
    }

}