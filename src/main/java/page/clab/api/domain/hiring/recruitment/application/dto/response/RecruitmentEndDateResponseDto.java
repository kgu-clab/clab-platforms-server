package page.clab.api.domain.hiring.recruitment.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.hiring.application.domain.ApplicationType;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

import java.util.List;

@Getter
@Builder
public class RecruitmentEndDateResponseDto {

    private Long id;
    private ApplicationType applicationType;

    public static List<RecruitmentEndDateResponseDto> toDto(List<Recruitment> recruitments) {
        return recruitments.stream()
                .map(RecruitmentEndDateResponseDto::toDto)
                .toList();
    }

    public static RecruitmentEndDateResponseDto toDto(Recruitment recruitment) {
        return RecruitmentEndDateResponseDto.builder()
                .id(recruitment.getId())
                .applicationType(recruitment.getApplicationType())
                .build();
    }
}
