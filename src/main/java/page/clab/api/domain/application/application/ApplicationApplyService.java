package page.clab.api.domain.application.application;

import page.clab.api.domain.application.dto.request.ApplicationRequestDto;

public interface ApplicationApplyService {
    String apply(ApplicationRequestDto requestDto);
}