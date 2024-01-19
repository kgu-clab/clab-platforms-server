package page.clab.api.domain.executive.domain;

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
import page.clab.api.domain.executive.dto.request.ExecutivesRequestDto;
import page.clab.api.domain.member.domain.Member;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Executive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private ExecutivePosition position;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.executive.year}")
    private String year;

    public static Executive of(ExecutivesRequestDto executivesRequestDto) {
        return Executive.builder()
                .member(Member.builder().id(executivesRequestDto.getMemberId()).build())
                .position(executivesRequestDto.getPosition())
                .year(executivesRequestDto.getYear())
                .build();
    }

}
