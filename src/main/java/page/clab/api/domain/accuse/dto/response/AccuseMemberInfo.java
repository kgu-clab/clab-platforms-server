package page.clab.api.domain.accuse.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.accuse.domain.Accuse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AccuseMemberInfo {

    private String memberId;

    private String name;

    private LocalDateTime createdAt;

    public static List<AccuseMemberInfo> create(List<Accuse> accuses) {
        return accuses.stream()
                .map(AccuseMemberInfo::create)
                .toList();
    }

    public static AccuseMemberInfo create(Accuse accuse) {
        return AccuseMemberInfo.builder()
                .memberId(accuse.getMember().getId())
                .name(accuse.getMember().getName())
                .createdAt(accuse.getCreatedAt())
                .build();
    }

}
