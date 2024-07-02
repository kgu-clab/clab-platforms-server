package page.clab.api.domain.recruitment.application.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.notification.application.NotificationSenderUseCase;
import page.clab.api.domain.recruitment.application.RecruitmentRegisterUseCase;
import page.clab.api.domain.recruitment.dao.RecruitmentRepository;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.domain.RecruitmentStatus;
import page.clab.api.domain.recruitment.dto.request.RecruitmentRequestDto;
import page.clab.api.global.validation.ValidationService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentRegisterService implements RecruitmentRegisterUseCase {

    private final NotificationSenderUseCase notificationService;
    private final ValidationService validationService;
    private final RecruitmentRepository recruitmentRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManager entityManager;
    private final TransactionDefinition transactionDefinition;

    @Transactional
    @Override
    public Long register(RecruitmentRequestDto requestDto) {
        Recruitment recruitment = RecruitmentRequestDto.toEntity(requestDto);
        updateRecruitmentStatusByRecruitment(recruitment);
        validationService.checkValid(recruitment);
        notificationService.sendNotificationToAllMembers("새로운 모집 공고가 등록되었습니다.");
        return recruitmentRepository.save(recruitment).getId();
    }

    @Scheduled(cron = "0 * * * * *")
    public void updateRecruitmentStatus() {
        List<Recruitment> recruitments = recruitmentRepository.findAll();
        recruitments.forEach(this::updateRecruitmentStatusByRecruitment);
    }

    public void updateRecruitmentStatusByRecruitment(Recruitment recruitment) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        LocalDateTime now = LocalDateTime.now();
        RecruitmentStatus newStatus = RecruitmentStatus.OPEN;
        if (now.isBefore(recruitment.getStartDate())) {
            newStatus = RecruitmentStatus.UPCOMING;
        } else if(now.isAfter(recruitment.getEndDate())) {
            newStatus = RecruitmentStatus.CLOSED;
        }
        recruitment.updateStatus(newStatus);
        entityManager.merge(recruitment);
        transactionManager.commit(transactionStatus);
    }
}
