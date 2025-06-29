package page.clab.api.domain.members.support.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SupportCategory {
    BUG("BUG", "버그"),
    INQUIRY("INQUIRY", "문의");

    private final String key;
    private final String description;
}
