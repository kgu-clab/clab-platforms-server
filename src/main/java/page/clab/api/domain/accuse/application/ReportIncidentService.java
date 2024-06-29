package page.clab.api.domain.accuse.application;

import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;

public interface ReportIncidentService {
    Long reportIncident(AccuseRequestDto requestDto);
}
