package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ActivityGroupBoardResponseDto {

    private Long id;
    private String memberId;
    private String memberName;
    private Long parentId;
    private ActivityGroupBoardCategory category;
    private String title;
    private String content;
    private List<UploadedFileResponseDto> files;
    private LocalDateTime dueDateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ActivityGroupBoardResponseDto toDto(ActivityGroupBoard board, MemberDetailedInfoDto memberInfo) {
        return ActivityGroupBoardResponseDto.builder()
                .id(board.getId())
                .memberId(memberInfo.getMemberId())
                .memberName(memberInfo.getMemberName())
                .parentId(board.getParent() != null ? board.getParent().getId() : null)
                .category(board.getCategory())
                .title(board.getTitle())
                .content(board.getContent())
                .files(UploadedFileResponseDto.toDto(board.getUploadedFiles()))
                .dueDateTime(board.getDueDateTime())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    public static ActivityGroupBoardResponseDto toActivityGroupBoardResponseDtoWithMemberInfo(ActivityGroupBoard activityGroupBoard, ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase) {
        MemberDetailedInfoDto memberInfo = externalRetrieveMemberUseCase.getMemberDetailedInfoById(activityGroupBoard.getMemberId());
        return ActivityGroupBoardResponseDto.toDto(activityGroupBoard, memberInfo);
    }
}
