package page.clab.api.domain.members.support.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SupportStatus {

    PENDING("PENDING", "처리 전"),
    COMPLETED("COMPLETED", "처리 완료");

    @Enumerated(EnumType.STRING)
    private final String key;
    private final String description;
}
