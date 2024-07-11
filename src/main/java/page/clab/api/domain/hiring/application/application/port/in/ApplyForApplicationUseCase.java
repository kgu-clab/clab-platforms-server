package page.clab.api.domain.hiring.application.application.port.in;

import page.clab.api.domain.hiring.application.application.dto.request.ApplicationRequestDto;

public interface ApplyForApplicationUseCase {
    String applyForClub(ApplicationRequestDto requestDto);
}
