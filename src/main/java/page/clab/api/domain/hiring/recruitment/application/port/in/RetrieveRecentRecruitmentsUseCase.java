package page.clab.api.domain.hiring.recruitment.application.port.in;

import java.util.List;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentEndDateResponseDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentResponseDto;

public interface RetrieveRecentRecruitmentsUseCase {

    List<RecruitmentResponseDto> retrieveRecentRecruitments();

    List<RecruitmentEndDateResponseDto> retrieveRecruitmentsByEndDate();
}
