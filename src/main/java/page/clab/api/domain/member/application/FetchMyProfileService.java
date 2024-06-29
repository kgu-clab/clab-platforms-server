package page.clab.api.domain.member.application;

import page.clab.api.domain.member.dto.response.MyProfileResponseDto;

public interface FetchMyProfileService {
    MyProfileResponseDto fetchMyProfile();
}