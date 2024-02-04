package page.clab.api.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
