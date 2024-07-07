package page.clab.api.domain.application.application.port.in;

public interface ToggleApplicationApprovalUseCase {
    String toggleApprovalStatus(Long recruitmentId, String studentId);
}
