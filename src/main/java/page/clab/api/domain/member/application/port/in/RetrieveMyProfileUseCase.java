package page.clab.api.domain.member.application.port.in;

import page.clab.api.domain.member.application.dto.response.MyProfileResponseDto;

public interface RetrieveMyProfileUseCase {
    MyProfileResponseDto retrieveMyProfile();
}
