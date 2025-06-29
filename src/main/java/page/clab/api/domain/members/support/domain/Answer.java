package page.clab.api.domain.members.support.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import page.clab.api.domain.members.support.application.dto.request.AnswerUpdateRequestDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Answer {

    private Long id;
    private Long supportId;
    private String content;
    private String adminId;
    private String adminName;

    private Boolean isDeleted;
    private LocalDateTime createdAt;

    public void delete() {
        isDeleted = true;
    }

    public void update(AnswerUpdateRequestDto requestDto) {
        Optional.ofNullable(requestDto.getContent()).ifPresent(this::setContent);
    }
}
