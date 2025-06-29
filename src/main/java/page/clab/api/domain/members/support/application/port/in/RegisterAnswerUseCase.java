package page.clab.api.domain.members.support.application.port.in;

import page.clab.api.domain.members.support.application.dto.request.AnswerRequestDto;

public interface RegisterAnswerUseCase {
    Long registerAnswer(Long supportId, AnswerRequestDto requestDto);
}
