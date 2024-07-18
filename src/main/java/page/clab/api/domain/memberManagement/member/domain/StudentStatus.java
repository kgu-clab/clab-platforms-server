package page.clab.api.domain.memberManagement.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudentStatus {

    CURRENT("CURRENT", "재학생"),
    ON_LEAVE("ON_LEAVE", "휴학생"),
    GRADUATED("GRADUATED", "졸업생");

    private String key;
    private String description;
}
