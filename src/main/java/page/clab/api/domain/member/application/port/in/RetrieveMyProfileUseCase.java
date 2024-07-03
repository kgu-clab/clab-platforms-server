package page.clab.api.domain.member.application.port.in;

import page.clab.api.domain.member.dto.response.MyProfileResponseDto;

public interface RetrieveMyProfileUseCase {
    MyProfileResponseDto retrieve();
}
