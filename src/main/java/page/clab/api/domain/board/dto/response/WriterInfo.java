package page.clab.api.domain.board.dto.response;

import lombok.Getter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;

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

    public static WriterInfo fromBoard(Board board, MemberDetailedInfoDto memberInfo) {
        if (memberInfo.isAdminRole() && board.isNotice()) {
            return new WriterInfo(null, "운영진");
        } else if (board.isWantAnonymous()) {
            return new WriterInfo(null, board.getNickname());
        }
        return new WriterInfo(memberInfo.getMemberId(), memberInfo.getMemberName());
    }

    public static WriterInfo fromBoardDetails(Board board, MemberDetailedInfoDto memberInfo) {
        if (memberInfo.isAdminRole() && board.isNotice()) {
            return new WriterInfo(null, "운영진", memberInfo.getRoleLevel(), null);
        } else if (board.isWantAnonymous()) {
            return new WriterInfo(null, board.getNickname(), null, null);
        }
        return new WriterInfo(memberInfo.getMemberId(), memberInfo.getMemberName(), memberInfo.getRoleLevel(), memberInfo.getImageUrl());
    }

}
