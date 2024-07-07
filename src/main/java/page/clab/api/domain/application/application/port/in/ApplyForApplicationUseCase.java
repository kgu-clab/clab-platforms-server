package page.clab.api.domain.application.application.port.in;

import page.clab.api.domain.application.dto.request.ApplicationRequestDto;

public interface ApplyForApplicationUseCase {
    String applyForClub(ApplicationRequestDto requestDto);
}
