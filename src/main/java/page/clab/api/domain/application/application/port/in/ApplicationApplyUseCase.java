package page.clab.api.domain.application.application.port.in;

import page.clab.api.domain.application.dto.request.ApplicationRequestDto;

public interface ApplicationApplyUseCase {
    String apply(ApplicationRequestDto requestDto);
}