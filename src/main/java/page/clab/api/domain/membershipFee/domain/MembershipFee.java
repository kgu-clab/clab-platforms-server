package page.clab.api.domain.membershipFee.domain;

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
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false, length = 1000)
    @Size(min = 1, max = 1000, message = "{size.membershipFee.content}")
    private String content;

    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static MembershipFee of(MembershipFeeRequestDto membershipFeeRequestDto) {
        return ModelMapperUtil.getModelMapper().map(membershipFeeRequestDto, MembershipFee.class);
    }

    public void update(MembershipFeeUpdateRequestDto membershipFeeUpdateRequestDto) {
        Optional.ofNullable(membershipFeeUpdateRequestDto.getCategory()).ifPresent(this::setCategory);
        Optional.ofNullable(membershipFeeUpdateRequestDto.getAmount()).ifPresent(this::setAmount);
        Optional.ofNullable(membershipFeeUpdateRequestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(membershipFeeUpdateRequestDto.getImageUrl()).ifPresent(this::setImageUrl);
    }

}