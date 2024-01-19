package page.clab.api.domain.comment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.dto.request.CommentRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member writer;

    @Column(nullable = false, length = 1000)
    @Size(min = 1, max = 1000, message = "{size.comment.content}")
    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties("children")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @JsonIgnoreProperties("parent")
    private List<Comment> children = new ArrayList<>();

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static Comment of(CommentRequestDto commentRequestDto) {
        return ModelMapperUtil.getModelMapper().map(commentRequestDto, Comment.class);
    }

}