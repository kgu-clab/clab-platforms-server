package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.RecruitmentRepository;
import page.clab.api.type.dto.RecruitmentRequestDto;
import page.clab.api.type.dto.RecruitmentResponseDto;
import page.clab.api.type.entity.Recruitment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final MemberService memberService;

    private final RecruitmentRepository recruitmentRepository;

    public void createRecruitment(RecruitmentRequestDto recruitmentRequestDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Recruitment recruitment = Recruitment.of(recruitmentRequestDto);
        recruitmentRepository.save(recruitment);
    }

    public List<RecruitmentResponseDto> getRecruitments() {
        List<Recruitment> recruitments = recruitmentRepository.findAll();
        return recruitments.stream()
                .map(RecruitmentResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<RecruitmentResponseDto> getRecentRecruitments() {
        List<Recruitment> recruitments = recruitmentRepository.findTop5ByOrderByCreatedAtDesc();
        return recruitments.stream()
                .map(RecruitmentResponseDto::of)
                .collect(Collectors.toList());
    }

    public void updateRecruitment(Long recruitmentId, RecruitmentRequestDto recruitmentRequestDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Recruitment recruitment = getRecruitmentByIdOrThrow(recruitmentId);
        Recruitment updatedRecruitment = Recruitment.of(recruitmentRequestDto);
        updatedRecruitment.setId(recruitment.getId());
        updatedRecruitment.setCreatedAt(recruitment.getCreatedAt());
        updatedRecruitment.setUpdatedAt(LocalDateTime.now());
        recruitmentRepository.save(updatedRecruitment);
    }

    public void deleteRecruitment(Long recruitmentId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Recruitment recruitment = getRecruitmentByIdOrThrow(recruitmentId);
        recruitmentRepository.delete(recruitment);
    }

    public void deleteAllRecruitment() throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        recruitmentRepository.deleteAll();
    }

    private Recruitment getRecruitmentByIdOrThrow(Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new NotFoundException("해당 모집 공고가 존재하지 않습니다."));
    }

}
