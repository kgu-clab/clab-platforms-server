package page.clab.api.domain.hiring.application.application.port.in;

public interface RejectApplicationUseCase {

    Long rejectApplication(Long recruitmentId, String studentId);
}
