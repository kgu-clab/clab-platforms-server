package page.clab.api.domain.recruitment.application;

import page.clab.api.domain.recruitment.dto.response.RecruitmentResponseDto;

import java.util.List;

public interface RecentRecruitmentsRetrievalService {
    List<RecruitmentResponseDto> retrieve();
}
