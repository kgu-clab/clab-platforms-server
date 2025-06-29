package page.clab.api.domain.members.support.application.port.in;

import page.clab.api.domain.members.support.application.dto.response.SupportDetailsResponseDto;

public interface RetrieveSupportDetailsUseCase {
    SupportDetailsResponseDto retrieveSupportDetails(Long supportId);
}
