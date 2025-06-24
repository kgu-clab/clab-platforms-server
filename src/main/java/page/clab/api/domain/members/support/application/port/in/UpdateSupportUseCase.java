package page.clab.api.domain.members.support.application.port.in;

import jakarta.validation.Valid;
import page.clab.api.domain.members.support.application.dto.request.SupportUpdateRequestDTO;

public interface UpdateSupportUseCase {
    Long updateSupport(Long supportId, @Valid SupportUpdateRequestDTO requestDto);
}
