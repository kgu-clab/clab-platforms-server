package page.clab.api.domain.hiring.recruitment.application.port.in;

import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentDetailsResponseDto;

public interface RetrieveRecruitmentUseCase {
    RecruitmentDetailsResponseDto retrieveRecruitmentDetails(Long recruitmentId);
}
