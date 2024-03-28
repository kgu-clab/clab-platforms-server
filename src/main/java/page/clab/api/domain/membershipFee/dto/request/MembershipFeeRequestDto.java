package page.clab.api.domain.membershipFee.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipFeeRequestDto {

    @NotNull(message = "{notNull.membershipFee.category}")
    @Schema(description = "카테고리", example = "지출", required = true)
    private String category;

    @NotNull(message = "{notNull.membershipFee.amount}")
    @Schema(description = "금액", example = "10000", required = true)
    private Long amount;

    @NotNull(message = "{notNull.membershipFee.content}")
    @Schema(description = "내용", example = "2023-2 동아리 종강총회", required = true)
    private String content;

    @Schema(description = "증빙 사진", example = "https://images.chosun.com/resizer/mcbrEkwTr5YKQZ89QPO9hmdb0iE=/616x0/smart/cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/LPCZYYKZ4FFIJPDD344FSGCLCY.jpg")
    private String imageUrl;

    public static MembershipFee toEntity(MembershipFeeRequestDto requestDto, Member member) {
        return MembershipFee.builder()
                .applicant(member)
                .category(requestDto.getCategory())
                .amount(requestDto.getAmount())
                .content(requestDto.getContent())
                .imageUrl(requestDto.getImageUrl())
                .status(MembershipFeeStatus.PENDING)
                .build();
    }

}
