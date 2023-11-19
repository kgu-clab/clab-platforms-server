package page.clab.api.type.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.dto.AccuseRequestDto;
import page.clab.api.type.etc.AccuseStatus;
import page.clab.api.type.etc.AccuseType;
import page.clab.api.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Accuse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Column(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private AccuseType accuseType;

    @Column(nullable = false)
    private Long targetId;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private AccuseStatus accuseStatus;

    @Column(nullable = false)
    private LocalDateTime accusedAt;

    public static Accuse of(AccuseRequestDto accuseRequestDto) {
        return ModelMapperUtil.getModelMapper().map(accuseRequestDto, Accuse.class);
    }

}
