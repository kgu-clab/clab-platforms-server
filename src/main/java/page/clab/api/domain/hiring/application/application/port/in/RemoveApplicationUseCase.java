package page.clab.api.domain.hiring.application.application.port.in;

public interface RemoveApplicationUseCase {
    String removeApplication(Long recruitmentId, String studentId);
}
