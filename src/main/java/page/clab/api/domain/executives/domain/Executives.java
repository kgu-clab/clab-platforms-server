package page.clab.api.domain.executives.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.executives.dto.request.ExecutivesRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Executives {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private ExecutivesPosition position;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.executives.year}")
    private String year;

    public static Executives of(ExecutivesRequestDto executivesRequestDto) {
        Executives executives = ModelMapperUtil.getModelMapper().map(executivesRequestDto, Executives.class);
        executives.setMember(Member.builder().id(executivesRequestDto.getMemberId()).build());
        return executives;
    }

}
