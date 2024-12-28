package page.clab.api.domain.hiring.recruitment.application.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.application.port.out.UpdateRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;
import page.clab.api.domain.hiring.recruitment.domain.RecruitmentStatus;

@Component
@RequiredArgsConstructor
public class RecruitmentStatusUpdater {

    private final RetrieveRecruitmentPort retrieveRecruitmentPort;
    private final UpdateRecruitmentPort updateRecruitmentPort;

    @Scheduled(cron = "0 */5 * * * *")
    public void updateRecruitmentStatus() {
        List<Recruitment> recruitments = retrieveRecruitmentPort.findAll();
        recruitments.forEach(this::updateRecruitmentStatusByRecruitment);
    }

    public void updateRecruitmentStatusByRecruitment(Recruitment recruitment) {
        updateRecruitmentStatus(recruitment);
        updateRecruitmentPort.update(recruitment);
    }

    public void updateRecruitmentStatus(Recruitment recruitment) {
        LocalDateTime now = LocalDateTime.now();
        RecruitmentStatus newStatus = RecruitmentStatus.OPEN;
        if (now.isBefore(recruitment.getStartDate())) {
            newStatus = RecruitmentStatus.UPCOMING;
        } else if (now.isAfter(recruitment.getEndDate())) {
            newStatus = RecruitmentStatus.CLOSED;
        }
        recruitment.updateStatus(newStatus);
    }
}
