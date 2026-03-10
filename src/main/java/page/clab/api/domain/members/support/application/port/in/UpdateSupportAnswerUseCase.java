package page.clab.api.domain.members.support.application.port.in;

import jakarta.validation.Valid;
import page.clab.api.domain.members.support.application.dto.request.SupportAnswerUpdateRequestDto;

public interface UpdateSupportAnswerUseCase {
    Long updateAnswer(Long supportId, @Valid SupportAnswerUpdateRequestDto requestDto);
}
