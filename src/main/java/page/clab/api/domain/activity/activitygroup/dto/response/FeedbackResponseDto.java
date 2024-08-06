package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class FeedbackResponseDto {

    private Long id;
    private String memberId;
    private String memberName;
    private String content;
    private List<UploadedFileResponseDto> files;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FeedbackResponseDto toDto(ActivityGroupBoard board, MemberDetailedInfoDto memberInfo) {
        return FeedbackResponseDto.builder()
                .id(board.getId())
                .memberId(memberInfo.getMemberId())
                .memberName(memberInfo.getMemberName())
                .content(board.getContent())
                .files(UploadedFileResponseDto.toDto(board.getUploadedFiles()))
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    public static FeedbackResponseDto toFeedbackResponseDtoWithMemberInfo(ActivityGroupBoard activityGroupBoard, ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase) {
        MemberDetailedInfoDto memberInfo = externalRetrieveMemberUseCase.getMemberDetailedInfoById(activityGroupBoard.getMemberId());
        return FeedbackResponseDto.toDto(activityGroupBoard, memberInfo);
    }
}
