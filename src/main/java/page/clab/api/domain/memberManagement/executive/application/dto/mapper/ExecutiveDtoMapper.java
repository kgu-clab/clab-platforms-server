package page.clab.api.domain.memberManagement.executive.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.executive.application.dto.request.ExecutiveRequestDto;
import page.clab.api.domain.memberManagement.executive.application.dto.response.ExecutiveResponseDto;
import page.clab.api.domain.memberManagement.executive.domain.Executive;

@Component
public class ExecutiveDtoMapper {

    public Executive fromDto(ExecutiveRequestDto requestDto) {
        return Executive.builder()
            .id(requestDto.getId())
            .name(requestDto.getName())
            .email(requestDto.getEmail())
            .field(requestDto.getField())
            .position(requestDto.getPosition())
            .imageUrl(requestDto.getImageUrl())
            .isDeleted(false)
            .build();
    }

    public ExecutiveResponseDto toDto(Executive executive) {
        return ExecutiveResponseDto.builder()
            .id(executive.getId())
            .name(executive.getName())
            .email(executive.getEmail())
            .field(executive.getField())
            .position(executive.getPosition())
            .imageUrl(executive.getImageUrl())
            .build();
    }
}
