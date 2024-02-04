package page.clab.api.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyProfileResponseDto {

    private String name;

    private String id;

    private String interests;

    private String contact;

    private String email;

    private String address;

    private String githubUrl;

    private String imageUrl;

    public static MyProfileResponseDto of(Member member) {
        return ModelMapperUtil.getModelMapper().map(member, MyProfileResponseDto.class);
    }

}
