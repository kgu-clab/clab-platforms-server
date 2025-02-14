package page.clab.api.global.common.notificationSetting.application.dto.notification;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;

@Getter
@Builder
public class BoardNotificationInfo {

    private String title;
    private String category;
    private String username;

    public static BoardNotificationInfo create(Board board, MemberDetailedInfoDto memberInfo) {
        return BoardNotificationInfo.builder()
            .title(board.getTitle())
            .category(board.getCategory().getDescription())
            .username(board.isWantAnonymous() ? board.getNickname()
                : memberInfo.getMemberId() + " " + memberInfo.getMemberName())
            .build();
    }
}
