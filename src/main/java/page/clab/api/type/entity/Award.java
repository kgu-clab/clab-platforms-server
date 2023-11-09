package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.dto.AwardRequestDto;
import page.clab.api.util.ModelMapperUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String competitionName;

    @Column(nullable = false)
    private String organizer;

    @Column(nullable = false)
    private String awardName;

    @Column(nullable = false)
    private LocalDateTime awardDate;

    @ManyToOne
    private Member member;

    public static Award of(AwardRequestDto awardRequestDto) {
        return ModelMapperUtil.getModelMapper().map(awardRequestDto, Award.class);
    }

}
