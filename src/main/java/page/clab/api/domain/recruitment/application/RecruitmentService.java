package page.clab.api.domain.recruitment.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.recruitment.dao.RecruitmentRepository;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.dto.request.RecruitmentRequestDto;
import page.clab.api.domain.recruitment.dto.request.RecruitmentUpdateRequestDto;
import page.clab.api.domain.recruitment.dto.response.RecruitmentResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final NotificationService notificationService;

    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    public Long createRecruitment(RecruitmentRequestDto recruitmentRequestDto) {
        Recruitment recruitment = Recruitment.of(recruitmentRequestDto);
        Long id = recruitmentRepository.save(recruitment).getId();
        notificationService.sendNotificationToAllMembers("새로운 모집 공고가 등록되었습니다.");
        return id;
    }

    public List<RecruitmentResponseDto> getRecentRecruitments() {
        List<Recruitment> recruitments = recruitmentRepository.findTop5ByOrderByCreatedAtDesc();
        return recruitments.stream()
                .map(RecruitmentResponseDto::of)
                .collect(Collectors.toList());
    }

    public Long updateRecruitment(Long recruitmentId, RecruitmentUpdateRequestDto recruitmentUpdateRequestDto) {
        Recruitment recruitment = getRecruitmentByIdOrThrow(recruitmentId);
        recruitment.update(recruitmentUpdateRequestDto);
        return recruitmentRepository.save(recruitment).getId();
    }

    public Long deleteRecruitment(Long recruitmentId) {
        Recruitment recruitment = getRecruitmentByIdOrThrow(recruitmentId);
        recruitmentRepository.delete(recruitment);
        return recruitment.getId();
    }

    public Recruitment getRecruitmentByIdOrThrow(Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new NotFoundException("해당 모집 공고가 존재하지 않습니다."));
    }

}
