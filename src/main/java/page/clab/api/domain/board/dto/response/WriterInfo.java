package page.clab.api.domain.board.dto.response;

import lombok.Getter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.Role;

@Getter
public class WriterInfo {

    private String id;

    private String name;

    private Long roleLevel;

    private String imageUrl;

    public WriterInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public WriterInfo(String id, String name, Long roleLevel, String imageUrl) {
        this.id = id;
        this.name = name;
        this.roleLevel = roleLevel;
        this.imageUrl = imageUrl;
    }

    public static WriterInfo fromBoard(Board board, Member member) {
        if (member.isAdminRole() && board.isNotice()) {
            return new WriterInfo(null, "운영진");
        } else if (board.isWantAnonymous()) {
            return new WriterInfo(null, board.getNickname());
        }
        return new WriterInfo(member.getId(), member.getName());
    }

    public static WriterInfo fromBoardDetails(Board board, Member member) {
        if (member.isAdminRole() && board.isNotice()) {
            return new WriterInfo(null, "운영진", Role.ADMIN.toRoleLevel(), null);
        } else if (board.isWantAnonymous()) {
            return new WriterInfo(null, board.getNickname(), null, null);
        }
        return new WriterInfo(member.getId(), member.getName(), member.getRole().toRoleLevel(), member.getImageUrl());
    }

}
