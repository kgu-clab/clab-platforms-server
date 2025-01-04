package page.clab.api.domain.memberManagement.executive.application.port.in;

import page.clab.api.domain.memberManagement.executive.application.dto.request.ExecutiveUpdateRequestDto;

public interface UpdateExecutiveUseCase {
    String updateExecutive(String executiveId, ExecutiveUpdateRequestDto requestDto);
}
