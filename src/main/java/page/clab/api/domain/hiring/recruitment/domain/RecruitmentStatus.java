package page.clab.api.domain.hiring.recruitment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecruitmentStatus {

    UPCOMING("UPCOMING", "모집 예정"),
    OPEN("OPEN", "진행중"),
    CLOSED("CLOSED", "종료");

    private final String key;
    private final String description;

    public boolean isRecruiting() {
        return this.equals(RecruitmentStatus.OPEN);
    }
}
