package page.clab.api.domain.memberManagement.member.application.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberBirthdayResponseDto {

    private String id;
    private String name;
    private LocalDate birth;
    private String imageUrl;
}
