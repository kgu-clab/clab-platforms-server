package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccuseStatus {

    PENDING("PENDING", "처리 중"),
    APPROVED("APPROVED", "처리 완료"),
    REJECTED("REJECTED", "거부");

    private String key;
    private String description;

}
