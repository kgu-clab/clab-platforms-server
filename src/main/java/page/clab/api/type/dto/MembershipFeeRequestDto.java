package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.entity.MembershipFee;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipFeeRequestDto {

    @NotNull(message = "{notNull.membershipFee.category}")
    @Size(min = 1, message = "{size.membershipFee.category}")
    private String category;

    @NotNull(message = "{notNull.membershipFee.content}")
    @Size(min = 1, max = 1000, message = "{size.membershipFee.content}")
    private String content;

    @URL(message = "{url.membershipFee.imageUrl}")
    private String imageUrl;

    public static MembershipFeeRequestDto of(MembershipFee membershipFee) {
        return ModelMapperUtil.getModelMapper().map(membershipFee, MembershipFeeRequestDto.class);
    }

}
