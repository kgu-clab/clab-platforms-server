package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.RecruitmentRepository;
import page.clab.api.type.dto.MemberResponseDto;
import page.clab.api.type.dto.NotificationRequestDto;
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

    private final NotificationService notificationService;

    private final RecruitmentRepository recruitmentRepository;

    public Long createRecruitment(RecruitmentRequestDto recruitmentRequestDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Recruitment recruitment = Recruitment.of(recruitmentRequestDto);
        Long id = recruitmentRepository.save(recruitment).getId();

        List<MemberResponseDto> members = memberService.getMembers();
        for (MemberResponseDto memberResponseDto : members) {
            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                    .memberId(memberResponseDto.getId())
                    .content("새로운 모집 공고가 등록되었습니다.")
                    .build();
            notificationService.createNotification(notificationRequestDto);
        }
        return id;
    }

    public List<RecruitmentResponseDto> getRecentRecruitments() {
        List<Recruitment> recruitments = recruitmentRepository.findTop5ByOrderByCreatedAtDesc();
        return recruitments.stream()
                .map(RecruitmentResponseDto::of)
                .collect(Collectors.toList());
    }

    public Long updateRecruitment(Long recruitmentId, RecruitmentRequestDto recruitmentRequestDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Recruitment recruitment = getRecruitmentByIdOrThrow(recruitmentId);
        Recruitment updatedRecruitment = Recruitment.of(recruitmentRequestDto);
        updatedRecruitment.setId(recruitment.getId());
        updatedRecruitment.setCreatedAt(recruitment.getCreatedAt());
        updatedRecruitment.setUpdateTime(LocalDateTime.now());
        return recruitmentRepository.save(updatedRecruitment).getId();
    }

    public Long deleteRecruitment(Long recruitmentId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Recruitment recruitment = getRecruitmentByIdOrThrow(recruitmentId);
        recruitmentRepository.delete(recruitment);
        return recruitment.getId();
    }

    private Recruitment getRecruitmentByIdOrThrow(Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new NotFoundException("해당 모집 공고가 존재하지 않습니다."));
    }

}
