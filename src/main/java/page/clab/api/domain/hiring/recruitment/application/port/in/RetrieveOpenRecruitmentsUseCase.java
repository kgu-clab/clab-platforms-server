package page.clab.api.domain.hiring.recruitment.application.port.in;

import java.util.List;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentOpenResponseDto;

public interface RetrieveOpenRecruitmentsUseCase {
    List<RecruitmentOpenResponseDto> retrieveOpenRecruitments();
}
