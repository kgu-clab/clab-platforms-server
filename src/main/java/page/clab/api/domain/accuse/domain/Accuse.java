package page.clab.api.domain.accuse.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private boolean isDeleted;
    private LocalDateTime createdAt;

    public void updateReason(String reason) {
        this.reason = reason;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
