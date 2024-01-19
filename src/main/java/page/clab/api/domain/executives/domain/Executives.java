package page.clab.api.domain.executives.domain;

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
