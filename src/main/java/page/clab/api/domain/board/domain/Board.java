package page.clab.api.domain.board.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Size(min = 1, max = 50, message = "{size.board.category}")
    private String category;

    @Column(nullable = false)
    @Size(min = 1, max = 100, message = "{size.board.title}")
    private String title;

    @Column(nullable = false, length = 10000)
    @Size(min = 1, max = 10000, message = "{size.board.content}")
    private String content;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static Board of(BoardRequestDto boardRequestDto) {
        return ModelMapperUtil.getModelMapper().map(boardRequestDto, Board.class);
    }

}