package page.clab.api.domain.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardCategory category;

    @Column(nullable = false)
    @Size(min = 1, max = 100, message = "{size.board.title}")
    private String title;

    @Column(nullable = false)
    @Size(min = 1, max = 10000, message = "{size.board.content}")
    private String content;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_files")
    private List<UploadedFile> uploadedFiles;

    private String imageUrl;

    @Getter
    @Column(nullable = false)
    private boolean wantAnonymous;

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

    public boolean isGraduated() {
        return this.category.equals(BoardCategory.GRADUATED);
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
        if (this.isGraduated() && !currentMemberInfo.isGraduated()) {
            throw new PermissionDeniedException("졸업생 게시판은 졸업생만 작성할 수 있습니다.");
        }
    }

}