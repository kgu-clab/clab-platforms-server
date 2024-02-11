package page.clab.api.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberBirthdayResponseDto {

    private String id;

    private String name;

    private LocalDate birth;

    private String imageUrl;

    public static MemberBirthdayResponseDto of(Member member) {
        return ModelMapperUtil.getModelMapper().map(member, MemberBirthdayResponseDto.class);
    }

}
