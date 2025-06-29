package page.clab.api.domain.members.support.application.port.in;

import jakarta.validation.Valid;
import page.clab.api.domain.members.support.application.dto.request.SupportUpdateRequestDto;

public interface UpdateSupportUseCase {
    Long updateSupport(Long supportId, @Valid SupportUpdateRequestDto requestDto);
}
