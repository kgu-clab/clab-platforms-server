package page.clab.api.domain.hiring.recruitment.application.port.out;


import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

public interface UpdateRecruitmentPort {

    Recruitment update(Recruitment recruitment);
}
