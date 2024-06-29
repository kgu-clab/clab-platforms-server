package page.clab.api.domain.application.application;

public interface ApplicationApprovalToggleService {
    String toggleStatus(Long recruitmentId, String studentId);
}
