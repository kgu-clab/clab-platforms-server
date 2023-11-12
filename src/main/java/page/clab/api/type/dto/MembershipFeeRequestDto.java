package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "카테고리", example = "지출", required = true)
    private String category;

    @NotNull(message = "{notNull.membershipFee.content}")
    @Size(min = 1, max = 1000, message = "{size.membershipFee.content}")
    @Schema(description = "내용", example = "2023-2 동아리 종강총회", required = true)
    private String content;

    @URL(message = "{url.membershipFee.imageUrl}")
    @Schema(description = "증빙 사진", example = "https://images.chosun.com/resizer/mcbrEkwTr5YKQZ89QPO9hmdb0iE=/616x0/smart/cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/LPCZYYKZ4FFIJPDD344FSGCLCY.jpg")
    private String imageUrl;

    public static MembershipFeeRequestDto of(MembershipFee membershipFee) {
        return ModelMapperUtil.getModelMapper().map(membershipFee, MembershipFeeRequestDto.class);
    }

}
