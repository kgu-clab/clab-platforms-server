package page.clab.api.domain.community.board.domain;

import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.community.board.application.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.community.board.application.exception.InvalidBoardCategoryHashtagException;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Board {

    private Long id;
    private String memberId;
    private String nickname;
    private BoardCategory category;
    private String title;
    private String content;
    private List<UploadedFile> uploadedFiles;
    private String imageUrl;
    private boolean wantAnonymous;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    public void update(BoardUpdateRequestDto boardUpdateRequestDto) {
        Optional.ofNullable(boardUpdateRequestDto.getCategory()).ifPresent(this::setCategory);
        Optional.ofNullable(boardUpdateRequestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(boardUpdateRequestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(boardUpdateRequestDto.getImageUrl()).ifPresent(this::setImageUrl);
        Optional.of(boardUpdateRequestDto.isWantAnonymous()).ifPresent(this::setWantAnonymous);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isNotice() {
        return this.category.equals(BoardCategory.NOTICE);
    }

    public boolean isDevelopmentQna() {
        return this.category.equals(BoardCategory.DEVELOPMENT_QNA);
    }

    public boolean shouldNotifyForNewBoard(MemberDetailedInfoDto memberInfo) {
        return memberInfo.isAdminRole() && this.category.equals(BoardCategory.NOTICE); // Assuming 2 is Admin role level
    }

    public boolean isOwner(String memberId) {
        return this.memberId.equals(memberId);
    }

    public void validateAccessPermission(MemberDetailedInfoDto memberInfo) throws PermissionDeniedException {
        if (!isOwner(memberInfo.getMemberId()) && !memberInfo.isAdminRole()) {
            throw new PermissionDeniedException("해당 게시글을 수정할 권한이 없습니다.");
        }
    }

    public void validateAccessPermissionForCreation(MemberDetailedInfoDto currentMemberInfo) throws PermissionDeniedException {
        if (this.isNotice() && !currentMemberInfo.isAdminRole()) {
            throw new PermissionDeniedException("공지사항은 관리자만 작성할 수 있습니다.");
        }
    }

    public void validateBoardHashtagRegistration(List<Long> hashtagIdList) {
        if (!isDevelopmentQna() && (hashtagIdList != null && !hashtagIdList.isEmpty())) {
            throw new InvalidBoardCategoryHashtagException("개발질문 게시판에만 해시태그를 등록할 수 있습니다.");
        }
    }
}
