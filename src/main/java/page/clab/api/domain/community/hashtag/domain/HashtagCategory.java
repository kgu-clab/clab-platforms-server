package page.clab.api.domain.community.hashtag.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HashtagCategory {

    LANGUAGE("LANGUAGE", "언어"),
    FIELD("FIELD", "분야"),
    SKILL("SKILL", "기술"),
    ETC("ETC", "기타");

    private final String key;
    private final String description;
}
