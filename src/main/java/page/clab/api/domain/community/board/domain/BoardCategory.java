package page.clab.api.domain.community.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardCategory {

    NOTICE("notice", "공지사항"),
    FREE("free", "자유 게시판"),
    DEVELOPMENT_QNA("development_qna", "개발 질문 게시판"),
    INFORMATION_REVIEWS("information_reviews", "정보 및 후기 게시판"),
    ORGANIZATION("organization", "동아리 소식");

    private final String key;
    private final String description;
}
