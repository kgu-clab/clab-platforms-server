package page.clab.api.global.common.slack.domain;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;

@Getter
@Builder
public class SlackBoardInfo {

    private String title;
    private String category;
    private String username;

    public static SlackBoardInfo create(Board board, MemberDetailedInfoDto memberInfo) {
        return SlackBoardInfo.builder()
                .title(board.getTitle())
                .category(board.getCategory().getDescription())
                .username(board.isWantAnonymous() ? board.getNickname() : memberInfo.getMemberId() + " " + memberInfo.getMemberName())
                .build();
    }
}
