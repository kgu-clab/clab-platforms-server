package page.clab.api.domain.hiring.recruitment.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.recruitment.application.dto.request.RecruitmentRequestDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentOpenResponseDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentDetailsResponseDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentEndDateResponseDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentResponseDto;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

@Component
public class RecruitmentDtoMapper {

    public Recruitment fromDto(RecruitmentRequestDto requestDto) {
        return Recruitment.builder()
            .title(requestDto.getTitle())
            .teamIntroduction(requestDto.getTeamIntroduction())
            .startDate(requestDto.getStartDate())
            .endDate(requestDto.getEndDate())
            .processTimeline(requestDto.getProcessTimeline())
            .applicationType(requestDto.getApplicationType())
            .jobDescription(requestDto.getJobDescription())
            .target(requestDto.getTarget())
            .isDeleted(false)
            .build();
    }

    public RecruitmentResponseDto toDto(Recruitment recruitment) {
        return RecruitmentResponseDto.builder()
            .id(recruitment.getId())
            .title(recruitment.getTitle())
            .startDate(recruitment.getStartDate())
            .endDate(recruitment.getEndDate())
            .applicationType(recruitment.getApplicationType())
            .target(recruitment.getTarget())
            .status(recruitment.getStatus().getDescription())
            .updatedAt(recruitment.getUpdatedAt())
            .build();
    }

    public RecruitmentEndDateResponseDto toEndDateDto(Recruitment recruitment) {
        return RecruitmentEndDateResponseDto.builder()
            .id(recruitment.getId())
            .applicationType(recruitment.getApplicationType())
            .build();
    }

    public RecruitmentDetailsResponseDto toDetailsDto(Recruitment recruitment) {
        return RecruitmentDetailsResponseDto.builder()
            .id(recruitment.getId())
            .title(recruitment.getTitle())
            .teamIntroduction(recruitment.getTeamIntroduction())
            .startDate(recruitment.getStartDate())
            .endDate(recruitment.getEndDate())
            .processTimeline(recruitment.getProcessTimeline())
            .applicationType(recruitment.getApplicationType())
            .jobDescription(recruitment.getJobDescription())
            .target(recruitment.getTarget())
            .status(recruitment.getStatus().getDescription())
            .updatedAt(recruitment.getUpdatedAt())
            .build();
    }

    public RecruitmentOpenResponseDto toOpenDto(Recruitment recruitment) {
        return RecruitmentOpenResponseDto.builder()
            .id(recruitment.getId())
            .applicationType(recruitment.getApplicationType())
            .build();
    }
}
