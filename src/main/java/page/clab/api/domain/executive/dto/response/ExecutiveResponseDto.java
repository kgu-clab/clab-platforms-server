package page.clab.api.domain.executive.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.executive.domain.Executive;
import page.clab.api.domain.executive.domain.ExecutivePosition;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecutiveResponseDto {

    private Long id;

    private String name;

    private String email;

    private String imageUrl;

    private String interests;

    private String githubUrl;

    private ExecutivePosition position;

    private String year;

    public static ExecutiveResponseDto of(Executive executive) {
        ExecutiveResponseDto executiveResponseDto = ModelMapperUtil.getModelMapper().map(executive, ExecutiveResponseDto.class);
        Member member = executive.getMember();
        executiveResponseDto.setName(member.getName());
        executiveResponseDto.setEmail(member.getEmail());
        executiveResponseDto.setImageUrl(member.getImageUrl());
        executiveResponseDto.setInterests(member.getInterests());
        executiveResponseDto.setGithubUrl(member.getGithubUrl());
        return executiveResponseDto;
    }

}
