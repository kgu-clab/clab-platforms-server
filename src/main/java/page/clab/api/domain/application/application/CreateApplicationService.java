package page.clab.api.domain.application.application;

import page.clab.api.domain.application.dto.request.ApplicationRequestDto;

public interface CreateApplicationService {
    String execute(ApplicationRequestDto requestDto);
}