package page.clab.api.domain.community.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardCategory {

    NOTICE("notice", "공지사항"),
    FREE("free", "자유 게시판"),
    QNA("qna", "질문 게시판"),
    GRADUATED("graduated", "졸업생 게시판"),
    ORGANIZATION("organization", "동아리 소식");

    private String key;
    private String description;
}
