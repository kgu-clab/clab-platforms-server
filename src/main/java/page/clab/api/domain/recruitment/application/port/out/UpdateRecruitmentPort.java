package page.clab.api.domain.recruitment.application.port.out;

import page.clab.api.domain.recruitment.domain.Recruitment;

public interface UpdateRecruitmentPort {
    Recruitment update(Recruitment recruitment);
}
