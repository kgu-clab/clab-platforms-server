package page.clab.api.domain.membershipFee.domain;

import java.time.LocalDateTime;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;
import page.clab.api.global.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembershipFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member applicant;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.membershipFee.category}")
    private String category;

    @Column(nullable = false, length = 1000)
    @Size(min = 1, max = 1000, message = "{size.membershipFee.content}")
    private String content;

    @URL(message = "{url.membershipFee.imageUrl}")
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static MembershipFee of(MembershipFeeRequestDto membershipFeeRequestDto) {
        return ModelMapperUtil.getModelMapper().map(membershipFeeRequestDto, MembershipFee.class);
    }

}