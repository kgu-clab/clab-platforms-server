package page.clab.api.domain.recruitment.application.port.out;

import page.clab.api.domain.recruitment.domain.Recruitment;

public interface RegisterRecruitmentPort {
    Recruitment save(Recruitment recruitment);
}
