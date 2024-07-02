package page.clab.api.domain.accuse.application.port.in;

import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;

public interface AccusationReportUseCase {
    Long reportIncident(AccuseRequestDto requestDto);
}
