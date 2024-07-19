package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.domain.memberManagement.member.application.dto.response.MyProfileResponseDto;

public interface RetrieveMyProfileUseCase {
    MyProfileResponseDto retrieveMyProfile();
}
