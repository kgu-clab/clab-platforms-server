package page.clab.api.domain.community.accuse.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class Accuse {

    private Long id;
    private String memberId;
    private AccuseTarget target;
    private String reason;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    public void updateReason(String reason) {
        this.reason = reason;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
