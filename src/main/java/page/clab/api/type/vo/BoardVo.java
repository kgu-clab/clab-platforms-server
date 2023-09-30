package page.clab.api.type.vo;

import lombok.Setter;
import lombok.ToString;
import page.clab.api.type.entity.Board;

import java.time.LocalDateTime;

@Setter
@ToString
public class BoardVo {

    private String category;

    private String title;

    private String content;

    private String writer;

    private LocalDateTime updateTime;

    private LocalDateTime createdAt;

    public BoardVo(Board board) {
        this.category = board.getCategory();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter().getUsername();
        this.updateTime = board.getUpdateTime();
        this.createdAt = board.getCreatedAt();
    }
}
