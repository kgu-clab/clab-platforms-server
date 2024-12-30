package page.clab.api.domain.memberManagement.executive.domain;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.memberManagement.executive.application.dto.request.ExecutiveUpdateRequestDto;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Executive {

    private String id;
    private String name;
    private String email;
    private String field;
    private String imageUrl;
    private Boolean isDeleted;

    public void update(ExecutiveUpdateRequestDto requestDto) {
        Optional.ofNullable(requestDto.getName()).ifPresent(this::setName);
        Optional.ofNullable(requestDto.getEmail()).ifPresent(this::setEmail);
        Optional.ofNullable(requestDto.getField()).ifPresent(this::setField);
        Optional.ofNullable(requestDto.getImageUrl()).ifPresent(this::setImageUrl);
    }

    public void delete() {
        this.isDeleted = true;
    }
}
