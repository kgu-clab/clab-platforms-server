package page.clab.api.domain.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardCategory {

    NOTICE("NOTICE", "공지사항"),
    FREE("FREE", "자유 게시판"),
    QNA("QNA", "질문 게시판"),
    GRADUATED("GRADUATED", "졸업생 게시판");

    private String key;
    private String description;

}
