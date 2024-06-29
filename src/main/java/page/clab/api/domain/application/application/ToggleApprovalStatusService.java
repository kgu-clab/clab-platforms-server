package page.clab.api.domain.application.application;

public interface ToggleApprovalStatusService {
    String execute(Long recruitmentId, String studentId);
}
