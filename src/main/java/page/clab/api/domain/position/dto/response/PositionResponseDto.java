package page.clab.api.domain.position.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionResponseDto {

    private Long id;

    private String name;

    private String email;

    private String imageUrl;

    private String interests;

    private String githubUrl;

    private PositionType positionType;

    private String year;

    public static PositionResponseDto of(Position position) {
        PositionResponseDto positionResponseDto = ModelMapperUtil.getModelMapper().map(position, PositionResponseDto.class);
        Member member = position.getMember();
        positionResponseDto.setName(member.getName());
        positionResponseDto.setEmail(member.getEmail());
        positionResponseDto.setImageUrl(member.getImageUrl());
        positionResponseDto.setInterests(member.getInterests());
        positionResponseDto.setGithubUrl(member.getGithubUrl());
        return positionResponseDto;
    }

}
