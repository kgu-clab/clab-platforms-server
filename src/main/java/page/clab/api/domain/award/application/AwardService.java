package page.clab.api.domain.award.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.award.dao.AwardRepository;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.request.AwardRequestDto;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final MemberService memberService;

    private final AwardRepository awardRepository;

    public Long createAward(AwardRequestDto awardRequestDto) {
        Member member = memberService.getCurrentMember();
        Award award = Award.of(awardRequestDto);
        award.setMember(member);
        return awardRepository.save(award).getId();
    }

    public PagedResponseDto<AwardResponseDto> getMyAwards(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Award> awards = getAwardByMember(pageable, member);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::of));
    }

    @Transactional
    public PagedResponseDto<AwardResponseDto> searchAwards(String memberId, Long year, Pageable pageable) {
        Page<Award> awards = null;
        if (memberId != null) {
            Member member = memberService.getMemberByIdOrThrow(memberId);
            awards = getAwardByMember(pageable, member);
        } else if (year != null) {
            awards = getAwardByYear(pageable, year);
        } else {
            throw new NotFoundException("적어도 학번 혹은 연도 중 하나는 입력해야 합니다.");
        }
        return new PagedResponseDto<>(awards.map(AwardResponseDto::of));
    }

    public Long updateAward(Long awardId, AwardRequestDto awardRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Award award = getAwardByIdOrThrow(awardId);
        if (!(award.getMember().getId().equals(member.getId()) || memberService.isMemberSuperRole(member))) {
            throw new PermissionDeniedException("해당 수상 이력을 수정할 권한이 없습니다.");
        }
        Award updatedAward = Award.of(awardRequestDto);
        updatedAward.setId(award.getId());
        return awardRepository.save(updatedAward).getId();
    }

    public Long deleteAward(Long awardId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Award award = getAwardByIdOrThrow(awardId);
        if (!(award.getMember().getId().equals(member.getId()) || memberService.isMemberSuperRole(member))) {
            throw new PermissionDeniedException("해당 수상 이력을 수정할 권한이 없습니다.");
        }
        awardRepository.delete(award);
        return award.getId();
    }

    private Award getAwardByIdOrThrow(Long awardId) {
        return awardRepository.findById(awardId)
                .orElseThrow(() -> new NotFoundException("해당 수상 이력이 존재하지 않습니다."));
    }

    private Page<Award> getAwardByMember(Pageable pageable, Member member) {
        return awardRepository.findAllByMemberOrderByAwardDateDesc(member, pageable);
    }

    private Page<Award> getAwardByYear(Pageable pageable, Long year){
        LocalDate startOfYear = LocalDate.of(year.intValue(), 1, 1);
        LocalDate endOfYear = LocalDate.of(year.intValue(), 12, 31);
        return awardRepository.findAllByAwardDateBetween(startOfYear, endOfYear, pageable);
    }

}