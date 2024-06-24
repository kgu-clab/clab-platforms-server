package page.clab.api.domain.recruitment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecruitmentStatus {

    UPCOMING("UPCOMING", "모집 예정"),
    OPEN("OPEN", "모집중"),
    CLOSED("CLOSED", "모집 종료");

    private String key;
    private String description;

}
