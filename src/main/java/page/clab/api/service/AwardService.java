package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.AwardRepository;
import page.clab.api.type.dto.AwardRequestDto;
import page.clab.api.type.dto.AwardResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.Award;
import page.clab.api.type.entity.Member;

import javax.transaction.Transactional;
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
    public PagedResponseDto<AwardResponseDto> searchAwards(String memberId, Pageable pageable) {
        Member member = memberService.getMemberByIdOrThrow(memberId);
        Page<Award> awards = getAwardByMember(pageable, member);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::of));
    }
    @Transactional
    public PagedResponseDto<AwardResponseDto> searchAnnualAwards(int year, Pageable pageable){
        Page<Award> awards = getAwardByYear(pageable, year);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::of));
    }

    public Long updateAward(Long awardId, AwardRequestDto awardRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Award award = getAwardByIdOrThrow(awardId);
        if (!(award.getMember().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 수상 이력을 수정할 권한이 없습니다.");
        }
        Award updatedAward = Award.of(awardRequestDto);
        updatedAward.setId(award.getId());
        return awardRepository.save(updatedAward).getId();
    }

    public Long deleteAward(Long awardId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Award award = getAwardByIdOrThrow(awardId);
        if (!(award.getMember().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
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

    private Page<Award> getAwardByYear(Pageable pageable, int year){
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        return awardRepository.findAllByAwardDateBetween(startOfYear, endOfYear, pageable);
    }

}