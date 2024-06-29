package page.clab.api.domain.recruitment.application.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.recruitment.application.RecruitmentLookupService;
import page.clab.api.domain.recruitment.application.UpdateRecruitmentService;
import page.clab.api.domain.recruitment.dao.RecruitmentRepository;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.domain.RecruitmentStatus;
import page.clab.api.domain.recruitment.dto.request.RecruitmentUpdateRequestDto;
import page.clab.api.global.validation.ValidationService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateRecruitmentServiceImpl implements UpdateRecruitmentService {

    private final RecruitmentLookupService recruitmentLookupService;
    private final ValidationService validationService;
    private final RecruitmentRepository recruitmentRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManager entityManager;
    private final TransactionDefinition transactionDefinition;

    @Transactional
    @Override
    public Long execute(Long recruitmentId, RecruitmentUpdateRequestDto requestDto) {
        Recruitment recruitment = recruitmentLookupService.getRecruitmentByIdOrThrow(recruitmentId);
        recruitment.update(requestDto);
        updateRecruitmentStatusByRecruitment(recruitment);
        validationService.checkValid(recruitment);
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
