package page.clab.api.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDate;

@Getter
@Builder
public class MemberBirthdayResponseDto {

    private String id;

    private String name;

    private LocalDate birth;

    private String imageUrl;

    public static MemberBirthdayResponseDto toDto(Member member) {
        return MemberBirthdayResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .birth(member.getBirth())
                .imageUrl(member.getImageUrl())
                .build();
    }

}
