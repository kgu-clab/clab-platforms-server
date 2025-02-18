package page.clab.api.domain.activity.activitygroup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.CollectionUtils;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupBoardUpdateRequestDto;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE activity_group_board SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class ActivityGroupBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_group_id", nullable = false)
    private ActivityGroup activityGroup;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityGroupBoardCategory category;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ActivityGroupBoard parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<ActivityGroupBoard> children = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_group_board_files")
    private List<UploadedFile> uploadedFiles;

    @Future(message = "invalid.activityGroupBoard.dueDateTime")
    private LocalDateTime dueDateTime;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public void update(ActivityGroupBoardUpdateRequestDto requestDto, UploadedFileService uploadedFileService) {
        Optional.ofNullable(requestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(requestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(requestDto.getDueDateTime()).ifPresent(this::setDueDateTime);
        Optional.ofNullable(requestDto.getFileUrls())
            .ifPresent(urls -> this.setUploadedFiles(uploadedFileService.getUploadedFilesByUrls(urls)));
    }

    public void addChild(ActivityGroupBoard child) {
        this.children.add(child);
    }

    public boolean isOwner(String memberId) {
        return this.memberId.equals(memberId);
    }

    public void validateAccessPermission(Member member) {
        if (!isOwner(member.getId()) && !member.isAdminRole()) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 활동 그룹 게시판을 수정/삭제할 권한이 없습니다.");
        }
    }

    public boolean isAssignment() {
        return this.category.equals(ActivityGroupBoardCategory.ASSIGNMENT);
    }

    public boolean isFeedback() {
        return this.category.equals(ActivityGroupBoardCategory.FEEDBACK);
    }

    public void validateAccessPermission(Member member, List<GroupMember> leaders) {
        if (!member.isAdminRole() && !CollectionUtils.isEmpty(leaders) && leaders.stream()
            .noneMatch(leader -> leader.isOwner(member.getId()))) {
            if (this.isAssignment()) {
                throw new BaseException(ErrorCode.PERMISSION_DENIED, "과제 게시판에 접근할 권한이 없습니다.");
            }
        }
    }

    public void validateEssentialElementByCategory() {
        if (this.isAssignment()) {
            LocalDateTime dueDateTime = this.getDueDateTime();
            if (dueDateTime == null) {
                throw new BaseException(ErrorCode.ASSIGNMENT_BOARD_HAS_NO_DUE_DATE);
            }
        }
        if (this.isFeedback()) {
            String content = this.getContent();
            if (content.isEmpty()) {
                throw new BaseException(ErrorCode.FEEDBACK_BOARD_HAS_NO_CONTENT);
            }
        }
    }
}
