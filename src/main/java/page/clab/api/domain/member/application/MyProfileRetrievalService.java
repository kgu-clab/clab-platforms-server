package page.clab.api.domain.member.application;

import page.clab.api.domain.member.dto.response.MyProfileResponseDto;

public interface MyProfileRetrievalService {
    MyProfileResponseDto retrieve();
}