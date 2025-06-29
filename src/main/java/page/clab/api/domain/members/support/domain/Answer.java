package page.clab.api.domain.members.support.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.members.support.application.dto.request.AnswerUpdateRequestDto;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

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

    public void validateAccessPermission(Member member) {
        if (!member.isAdminRole()) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 게시글을 수정/삭제할 권한이 없습니다.");
        }
    }

    public void delete() {
        isDeleted = true;
    }

    public void update(AnswerUpdateRequestDto requestDto) {
        Optional.ofNullable(requestDto.getContent()).ifPresent(this::setContent);
    }
}
