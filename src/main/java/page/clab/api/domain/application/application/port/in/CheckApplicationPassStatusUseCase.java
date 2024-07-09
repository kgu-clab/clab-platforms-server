package page.clab.api.domain.application.application.port.in;

import page.clab.api.domain.application.application.dto.response.ApplicationPassResponseDto;

public interface CheckApplicationPassStatusUseCase {
    ApplicationPassResponseDto checkPassStatus(Long recruitmentId, String studentId);
}
