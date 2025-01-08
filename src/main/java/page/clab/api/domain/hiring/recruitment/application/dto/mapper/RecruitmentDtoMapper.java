package page.clab.api.domain.hiring.recruitment.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.recruitment.application.dto.request.RecruitmentRequestDto;
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
            .applicationType(requestDto.getApplicationType())
            .description(requestDto.getDescription())
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
}
