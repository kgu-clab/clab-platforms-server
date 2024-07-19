package page.clab.api.domain.hiring.application.application.port.in;

public interface ToggleApplicationApprovalUseCase {
    String toggleApprovalStatus(Long recruitmentId, String studentId);
}
