package page.clab.api.domain.memberManagement.position.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Position {

    private Long id;
    private String memberId;
    private PositionType positionType;
    private String year;
    private boolean isDeleted = false;

    public static Position create(String memberId) {
        return Position.builder()
                .memberId(memberId)
                .positionType(PositionType.MEMBER)
                .year(String.valueOf(LocalDate.now().getYear()))
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
