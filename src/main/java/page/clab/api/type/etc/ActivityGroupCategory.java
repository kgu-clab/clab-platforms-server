package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityGroupCategory {

    스터디("STUDY", "스터디"),
    프로젝트("PROJECT", "프로젝트"),
    서비스("SERVICE", "서비스");

    private String key;
    private String description;
}
