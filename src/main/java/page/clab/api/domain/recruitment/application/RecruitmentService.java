package page.clab.api.domain.recruitment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.recruitment.dao.RecruitmentRepository;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.dto.request.RecruitmentRequestDto;
import page.clab.api.domain.recruitment.dto.request.RecruitmentUpdateRequestDto;
import page.clab.api.domain.recruitment.dto.response.RecruitmentResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final NotificationService notificationService;

    private final ValidationService validationService;

    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    public Long createRecruitment(RecruitmentRequestDto requestDto) {
        Recruitment recruitment = RecruitmentRequestDto.toEntity(requestDto);
        validationService.checkValid(recruitment);
        notificationService.sendNotificationToAllMembers("새로운 모집 공고가 등록되었습니다.");
        return recruitmentRepository.save(recruitment).getId();
    }

    @Transactional(readOnly = true)
    public List<RecruitmentResponseDto> getRecentRecruitments() {
        List<Recruitment> recruitments = recruitmentRepository.findTop5ByOrderByCreatedAtDesc();
        return recruitments.stream()
                .map(RecruitmentResponseDto::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<RecruitmentResponseDto> getDeletedRecruitments(Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(recruitments
                .map(RecruitmentResponseDto::toDto));
    }

    @Transactional
    public Long updateRecruitment(Long recruitmentId, RecruitmentUpdateRequestDto requestDto) {
        Recruitment recruitment = getRecruitmentByIdOrThrow(recruitmentId);
        recruitment.update(requestDto);
        validationService.checkValid(recruitment);
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
