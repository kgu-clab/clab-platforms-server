package page.clab.api.domain.application.application;

import page.clab.api.domain.application.dto.response.ApplicationPassResponseDto;

public interface FetchApplicationPassService {
    ApplicationPassResponseDto execute(Long recruitmentId, String studentId);
}
