package page.clab.api.domain.members.support.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.support.application.dto.response.SupportListResponseDto;
import page.clab.api.domain.members.support.application.dto.shared.SupportAnswerInfoDto;
import page.clab.api.domain.members.support.domain.Support;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveSupportUseCase {

    Support getById(Long supportId);

    PagedResponseDto<SupportListResponseDto> retrieveSupports(Pageable pageable);

    SupportAnswerInfoDto getSupportAnswerInfoById(Long supportId);
}
