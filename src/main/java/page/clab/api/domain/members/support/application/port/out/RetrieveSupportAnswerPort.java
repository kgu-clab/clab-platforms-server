package page.clab.api.domain.members.support.application.port.out;

import page.clab.api.domain.members.support.domain.SupportAnswer;

public interface RetrieveSupportAnswerPort {
    SupportAnswer findAnswerBySupportId(Long id);
}
