package page.clab.api.domain.application.application.port.in;

public interface ApplicationApprovalToggleUseCase {
    String toggleStatus(Long recruitmentId, String studentId);
}
