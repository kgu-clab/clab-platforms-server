package page.clab.api.domain.memberManagement.executive.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.executive.application.dto.request.ExecutiveRequestDto;
import page.clab.api.domain.memberManagement.executive.application.dto.response.ExecutiveResponseDto;
import page.clab.api.domain.memberManagement.executive.domain.Executive;

@Component
public class ExecutiveDtoMapper {

    public Executive fromDto(ExecutiveRequestDto requestDto) {
        return Executive.builder()
            .id(requestDto.getExecutiveId())
            .name(requestDto.getName())
            .email(requestDto.getEmail())
            .field(requestDto.getField())
            .imageUrl(requestDto.getImageUrl())
            .isDeleted(false)
            .build();
    }

    public ExecutiveResponseDto toDto(Executive executive, String position) {
        return ExecutiveResponseDto.builder()
            .executiveId(executive.getId())
            .name(executive.getName())
            .email(executive.getEmail())
            .field(executive.getField())
            .position(position)
            .imageUrl(executive.getImageUrl())
            .build();
    }
}
