package page.clab.api.domain.community.accuse.application.port.in;


import page.clab.api.domain.community.accuse.application.dto.request.AccuseRequestDto;

public interface ReportAccusationUseCase {

    Long reportAccusation(AccuseRequestDto requestDto);
}
