package page.clab.api.domain.members.support.application.port.out;

import page.clab.api.domain.members.support.domain.Answer;

public interface RegisterAnswerPort {
    Answer save(Answer answer);
}
