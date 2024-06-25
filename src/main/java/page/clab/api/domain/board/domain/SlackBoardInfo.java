package page.clab.api.domain.board.domain;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.domain.Member;

@Getter
@Builder
public class SlackBoardInfo {

    private String title;

    private String category;

    private String username;

    public static SlackBoardInfo create(Board board, Member member) {
        return SlackBoardInfo.builder()
                .title(board.getTitle())
                .category(board.getCategory().getDescription())
                .username(board.isWantAnonymous() ? board.getNickname() : member.getId() + " " + member.getName())
                .build();
    }

}
