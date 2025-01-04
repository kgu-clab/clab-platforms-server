package page.clab.api.domain.memberManagement.executive.application.port.in;

import java.util.List;
import page.clab.api.domain.memberManagement.executive.application.dto.response.ExecutiveResponseDto;

public interface RetrieveExecutiveUseCase {
    List<ExecutiveResponseDto> retrieveExecutives();
}
