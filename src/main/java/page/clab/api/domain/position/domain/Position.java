package page.clab.api.domain.position.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.position.dto.request.PositionRequestDto;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Position extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private PositionType positionType;

    @Column(nullable = false)
    private String year;

    public static Position of(PositionRequestDto positionRequestDto) {
        return Position.builder()
                .member(Member.builder().id(positionRequestDto.getMemberId()).build())
                .positionType(positionRequestDto.getPositionType())
                .year(positionRequestDto.getYear())
                .build();
    }

}
