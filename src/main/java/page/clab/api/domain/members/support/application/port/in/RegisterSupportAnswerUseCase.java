package page.clab.api.domain.members.support.application.port.in;

import page.clab.api.domain.members.support.application.dto.request.SupportAnswerRequestDto;

public interface RegisterSupportAnswerUseCase {
    Long registerAnswer(Long supportId, SupportAnswerRequestDto requestDto);
}
