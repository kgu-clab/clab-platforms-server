package page.clab.api.domain.membershipFee.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;

@Getter
@Setter
public class MembershipFeeRequestDto {

    @NotNull(message = "{notNull.membershipFee.category}")
    @Schema(description = "카테고리", example = "지출", required = true)
    private String category;

    @Schema(description = "계좌", example = "110-123-456789")
    private String account;

    @NotNull(message = "{notNull.membershipFee.amount}")
    @Schema(description = "금액", example = "10000", required = true)
    private Long amount;

    @NotNull(message = "{notNull.membershipFee.content}")
    @Schema(description = "내용", example = "2023-2 동아리 종강총회", required = true)
    private String content;

    @Schema(description = "증빙 사진", example = "https://images.chosun.com/resizer/mcbrEkwTr5YKQZ89QPO9hmdb0iE=/616x0/smart/cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/LPCZYYKZ4FFIJPDD344FSGCLCY.jpg")
    private String imageUrl;

    public static MembershipFee toEntity(MembershipFeeRequestDto requestDto, String memberId) {
        return MembershipFee.builder()
                .memberId(memberId)
                .category(requestDto.getCategory())
                .account(requestDto.getAccount())
                .amount(requestDto.getAmount())
                .content(requestDto.getContent())
                .imageUrl(requestDto.getImageUrl())
                .status(MembershipFeeStatus.PENDING)
                .build();
    }

}
