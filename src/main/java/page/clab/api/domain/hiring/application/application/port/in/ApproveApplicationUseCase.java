package page.clab.api.domain.hiring.application.application.port.in;

public interface ApproveApplicationUseCase {
    Long approveApplication(Long recruitmentId, String studentId);
}
