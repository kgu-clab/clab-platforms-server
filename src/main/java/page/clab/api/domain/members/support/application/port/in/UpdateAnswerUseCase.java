package page.clab.api.domain.members.support.application.port.in;

import jakarta.validation.Valid;
import page.clab.api.domain.members.support.application.dto.request.AnswerUpdateRequestDto;

public interface UpdateAnswerUseCase {
    Long updateSupport(Long supportId, @Valid AnswerUpdateRequestDto requestDto);
}
