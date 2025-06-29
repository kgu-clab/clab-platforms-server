package page.clab.api.domain.members.support.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.members.support.application.dto.request.SupportUpdateRequestDto;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Support {

    private Long id;
    private String memberId;
    private String nickname;
    private String title;
    private String content;
    private List<UploadedFile> uploadedFiles;
    private boolean wantAnonymous;
    private SupportCategory category;
    private SupportStatus status;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    public void update(SupportUpdateRequestDto requestDto) {
        Optional.ofNullable(requestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(requestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(requestDto.getCategory()).ifPresent(this::setCategory);
    }

    public void delete() { this.isDeleted = true; }

    public boolean isOwner(String memberId) {
        return this.memberId.equals(memberId);
    }

    public void validateAccessPermission(Member member) {
        if (!isOwner(member.getId()) && !member.isAdminRole()) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 게시글을 수정/삭제할 권한이 없습니다.");
        }
    }

    public void markAsCompleted() {
        this.status = SupportStatus.COMPLETED;
    }

    public void markAsPending() {
        this.status = SupportStatus.PENDING;
    }
}
