package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.MembershipFeeRepository;
import page.clab.api.type.dto.MembershipFeeRequestDto;
import page.clab.api.type.dto.MembershipFeeResponseDto;
import page.clab.api.type.dto.MembershipFeeUpdateRequestDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.MembershipFee;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipFeeService {

    private final MemberService memberService;

    private final MembershipFeeRepository membershipFeeRepository;

    public void createMembershipFee(MembershipFeeRequestDto membershipFeeRequestDto) {
        MembershipFee membershipFee = MembershipFee.of(membershipFeeRequestDto);
        Member member = memberService.getCurrentMember();
        membershipFee.setApplicant(member);
        membershipFeeRepository.save(membershipFee);
    }

    public List<MembershipFeeResponseDto> getMembershipFees() {
        List<MembershipFee> membershipFees = membershipFeeRepository.findAll();
        return membershipFees.stream()
                .map(MembershipFeeResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<MembershipFeeResponseDto> searchMembershipFee(String category) {
        List<MembershipFee> membershipFees = new ArrayList<>();
        membershipFees = getMembershipFeeByCateroty(category);
        if (membershipFees.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return membershipFees.stream()
                .map(MembershipFeeResponseDto::of)
                .collect(Collectors.toList());
    }

    public void updateMembershipFee(MembershipFeeUpdateRequestDto membershipFeeUpdateRequestDto) throws PermissionDeniedException {
        MembershipFee membershipFee = getMembershipFeeByIdOrThrow(membershipFeeUpdateRequestDto.getId());
        if (isMembershipFeeOwner(membershipFee)) {
            MembershipFee updatedMembershipFee = MembershipFee.of(membershipFeeUpdateRequestDto);
            updatedMembershipFee.setCreatedAt(membershipFee.getCreatedAt());
            updatedMembershipFee.setApplicant(membershipFee.getApplicant());
            membershipFeeRepository.save(updatedMembershipFee);
        } else {
            throw new PermissionDeniedException();
        }
    }

    public void deleteMembershipFee(Long membershipFeeId) throws PermissionDeniedException {
        MembershipFee membershipFee = getMembershipFeeByIdOrThrow(membershipFeeId);
        if (isMembershipFeeOwner(membershipFee)) {
            membershipFeeRepository.delete(membershipFee);
        } else {
            throw new PermissionDeniedException();
        }
    }

    private List<MembershipFee> getMembershipFeeByCateroty(String category) {
        return membershipFeeRepository.findByCategory(category);
    }

    private MembershipFee getMembershipFeeByIdOrThrow(Long membershipFeeId) {
        return membershipFeeRepository.findById(membershipFeeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회비 내역입니다."));
    }

    private boolean isMembershipFeeOwner(MembershipFee membershipFee) {
        Member member = memberService.getCurrentMember();
        if (member.equals(membershipFee.getApplicant())) {
            return true;
        } else {
            return false;
        }
    }

}
