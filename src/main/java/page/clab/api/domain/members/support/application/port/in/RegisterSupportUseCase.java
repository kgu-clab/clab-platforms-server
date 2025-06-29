package page.clab.api.domain.members.support.application.port.in;

import page.clab.api.domain.members.support.application.dto.request.SupportRequestDto;

public interface RegisterSupportUseCase {
    Long registerSupport(SupportRequestDto requestDto);
}
