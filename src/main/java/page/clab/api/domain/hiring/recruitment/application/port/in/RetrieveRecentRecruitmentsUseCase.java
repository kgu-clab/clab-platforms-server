package page.clab.api.domain.hiring.recruitment.application.port.in;

import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentResponseDto;

import java.util.List;

public interface RetrieveRecentRecruitmentsUseCase {
    List<RecruitmentResponseDto> retrieveRecentRecruitments();
}
