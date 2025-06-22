package page.clab.api.domain.members.support.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SupportCategory {
    BUG("BUG", "버그"),
    INQUIRY("INQUIRY", "문의");

    @Enumerated(EnumType.STRING)
    private final String key;
    private final String description;
}
