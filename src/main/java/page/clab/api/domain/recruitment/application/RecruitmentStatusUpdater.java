package page.clab.api.domain.recruitment.application;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import page.clab.api.domain.recruitment.application.port.out.LoadRecruitmentPort;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.domain.RecruitmentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RecruitmentStatusUpdater {

    private final LoadRecruitmentPort loadRecruitmentPort;
    private final PlatformTransactionManager transactionManager;
    private final EntityManager entityManager;
    private final TransactionDefinition transactionDefinition;

    @Scheduled(cron = "0 * * * * *")
    public void updateRecruitmentStatus() {
        List<Recruitment> recruitments = loadRecruitmentPort.findAll();
        recruitments.forEach(this::updateRecruitmentStatusByRecruitment);
    }

    public void updateRecruitmentStatusByRecruitment(Recruitment recruitment) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        LocalDateTime now = LocalDateTime.now();
        RecruitmentStatus newStatus = RecruitmentStatus.OPEN;
        if (now.isBefore(recruitment.getStartDate())) {
            newStatus = RecruitmentStatus.UPCOMING;
        } else if (now.isAfter(recruitment.getEndDate())) {
            newStatus = RecruitmentStatus.CLOSED;
        }
        recruitment.updateStatus(newStatus);
        entityManager.merge(recruitment);
        transactionManager.commit(transactionStatus);
    }
}
