package page.clab.api.domain.memberManagement.executive.application.port.in;

import page.clab.api.domain.memberManagement.executive.application.dto.request.ExecutiveRequestDto;

public interface RegisterExecutiveUseCase {
    String registerExecutive(ExecutiveRequestDto requestDto);
}
