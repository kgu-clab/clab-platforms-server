package page.clab.api.domain.memberManagement.workExperience.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.workExperience.application.dto.request.WorkExperienceRequestDto;
import page.clab.api.domain.memberManagement.workExperience.application.dto.response.WorkExperienceResponseDto;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;

@Component
public class WorkExperienceDtoMapper {

    public WorkExperience fromDto(WorkExperienceRequestDto requestDto, String memberId) {
        return WorkExperience.builder()
                .companyName(requestDto.getCompanyName())
                .position(requestDto.getPosition())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .memberId(memberId)
                .isDeleted(false)
                .build();
    }

    public WorkExperienceResponseDto toDto(WorkExperience workExperience) {
        return WorkExperienceResponseDto.builder()
                .id(workExperience.getId())
                .companyName(workExperience.getCompanyName())
                .position(workExperience.getPosition())
                .startDate(workExperience.getStartDate())
                .endDate(workExperience.getEndDate())
                .build();
    }
}
