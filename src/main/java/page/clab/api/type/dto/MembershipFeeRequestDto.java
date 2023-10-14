package page.clab.api.type.dto;

import lombok.*;
import page.clab.api.type.entity.MembershipFee;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipFeeRequestDto {

    private String category;

    private String content;

    private String imageUrl;

    public static MembershipFeeRequestDto of(MembershipFee membershipFee) {
        return ModelMapperUtil.getModelMapper().map(membershipFee, MembershipFeeRequestDto.class);
    }

}
