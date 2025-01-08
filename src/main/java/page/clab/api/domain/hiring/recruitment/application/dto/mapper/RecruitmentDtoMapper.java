package page.clab.api.domain.hiring.recruitment.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.recruitment.application.dto.request.RecruitmentRequestDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentDetailsResponseDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentEndDateResponseDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentResponseDto;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

@Component
public class RecruitmentDtoMapper {

    public Recruitment fromDto(RecruitmentRequestDto requestDto) {
        return Recruitment.builder()
            .recruitmentTitle(requestDto.getRecruitmentTitle())
            .recruitmentDetail(requestDto.getRecruitmentDetail())
            .startDate(requestDto.getStartDate())
            .endDate(requestDto.getEndDate())
            .recruitmentSchedule(requestDto.getRecruitmentSchedule())
            .applicationType(requestDto.getApplicationType())
            .recruitmentDescription(requestDto.getRecruitmentDescription())
            .target(requestDto.getTarget())
            .isDeleted(false)
            .build();
    }

    public RecruitmentResponseDto toDto(Recruitment recruitment) {
        return RecruitmentResponseDto.builder()
            .id(recruitment.getId())
            .recruitmentTitle(recruitment.getRecruitmentTitle())
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
            .recruitmentTitle(recruitment.getRecruitmentTitle())
            .recruitmentDetail(recruitment.getRecruitmentDetail())
            .startDate(recruitment.getStartDate())
            .endDate(recruitment.getEndDate())
            .recruitmentSchedule(recruitment.getRecruitmentSchedule())
            .applicationType(recruitment.getApplicationType())
            .recruitmentDescription(recruitment.getRecruitmentDescription())
            .target(recruitment.getTarget())
            .status(recruitment.getStatus().getDescription())
            .updatedAt(recruitment.getUpdatedAt())
            .build();
    }
}
