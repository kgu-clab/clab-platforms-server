package page.clab.api.domain.application.application.port.in;

public interface ToggleApplicationApprovalUseCase {
    String toggleStatus(Long recruitmentId, String studentId);
}
