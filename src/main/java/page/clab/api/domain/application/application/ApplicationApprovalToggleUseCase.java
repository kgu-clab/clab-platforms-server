package page.clab.api.domain.application.application;

public interface ApplicationApprovalToggleUseCase {
    String toggleStatus(Long recruitmentId, String studentId);
}
