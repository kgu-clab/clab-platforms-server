package page.clab.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.MembershipFeeRepository;
import page.clab.api.type.dto.MembershipFeeRequestDto;
import page.clab.api.type.dto.MembershipFeeResponseDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.MembershipFee;

@Service
@RequiredArgsConstructor
public class MembershipFeeService {

    private final MemberService memberService;

    private final MembershipFeeRepository membershipFeeRepository;

    public void createMembershipFee(MembershipFeeRequestDto membershipFeeRequestDto) {
        Member member = memberService.getCurrentMember();
        MembershipFee membershipFee = MembershipFee.of(membershipFeeRequestDto);
        membershipFee.setApplicant(member);
        membershipFeeRepository.save(membershipFee);
    }

    public List<MembershipFeeResponseDto> getMembershipFees(Pageable pageable) {
        Page<MembershipFee> membershipFees = membershipFeeRepository.findAll(pageable);
        return membershipFees.map(MembershipFeeResponseDto::of).getContent();
    }

    public List<MembershipFeeResponseDto> searchMembershipFee(String category, Pageable pageable) {
        Page<MembershipFee> membershipFees;
        membershipFees = getMembershipFeeByCateroty(category, pageable);
        if (membershipFees.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return membershipFees.map(MembershipFeeResponseDto::of).getContent();
    }

    public void updateMembershipFee(Long membershipFeeId, MembershipFeeRequestDto membershipFeeRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        MembershipFee membershipFee = getMembershipFeeByIdOrThrow(membershipFeeId);
        if (!(membershipFee.getApplicant().equals(member) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException();
        }
        MembershipFee updatedMembershipFee = MembershipFee.of(membershipFeeRequestDto);
        updatedMembershipFee.setId(membershipFee.getId());
        updatedMembershipFee.setApplicant(membershipFee.getApplicant());
        updatedMembershipFee.setCreatedAt(membershipFee.getCreatedAt());
        membershipFeeRepository.save(updatedMembershipFee);
    }

    public void deleteMembershipFee(Long membershipFeeId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        MembershipFee membershipFee = getMembershipFeeByIdOrThrow(membershipFeeId);
        if (!(membershipFee.getApplicant().equals(member) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException();
        }
        membershipFeeRepository.delete(membershipFee);
    }

    private Page<MembershipFee> getMembershipFeeByCateroty(String category, Pageable pageable) {
        return membershipFeeRepository.findByCategory(category, pageable);
    }

    private MembershipFee getMembershipFeeByIdOrThrow(Long membershipFeeId) {
        return membershipFeeRepository.findById(membershipFeeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회비 내역입니다."));
    }

}
