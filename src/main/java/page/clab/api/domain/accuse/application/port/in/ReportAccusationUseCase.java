package page.clab.api.domain.accuse.application.port.in;

import page.clab.api.domain.accuse.application.dto.request.AccuseRequestDto;

public interface ReportAccusationUseCase {
    Long reportAccusation(AccuseRequestDto requestDto);
}
