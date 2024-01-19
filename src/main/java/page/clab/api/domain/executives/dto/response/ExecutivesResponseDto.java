package page.clab.api.domain.executives.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.executives.domain.Executives;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.executives.domain.ExecutivesPosition;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecutivesResponseDto {

    private Long id;

    private String name;

    private String email;

    private String imageUrl;

    private String interests;

    private String githubUrl;

    private ExecutivesPosition position;

    private String year;

    public static ExecutivesResponseDto of(Executives executives) {
        ExecutivesResponseDto executivesResponseDto = ModelMapperUtil.getModelMapper().map(executives, ExecutivesResponseDto.class);
        Member member = executives.getMember();
        executivesResponseDto.setName(member.getName());
        executivesResponseDto.setEmail(member.getEmail());
        executivesResponseDto.setImageUrl(member.getImageUrl());
        executivesResponseDto.setInterests(member.getInterests());
        executivesResponseDto.setGithubUrl(member.getGithubUrl());
        return executivesResponseDto;
    }

}
