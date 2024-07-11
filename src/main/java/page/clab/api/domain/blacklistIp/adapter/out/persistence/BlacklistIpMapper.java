package page.clab.api.domain.blacklistIp.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;

@Component
public class BlacklistIpMapper {

    public BlacklistIpJpaEntity toJpaEntity(BlacklistIp blacklistIp) {
        return BlacklistIpJpaEntity.builder()
                .id(blacklistIp.getId())
                .ipAddress(blacklistIp.getIpAddress())
                .reason(blacklistIp.getReason())
                .build();
    }

    public BlacklistIp toDomain(BlacklistIpJpaEntity entity) {
        return BlacklistIp.builder()
                .id(entity.getId())
                .ipAddress(entity.getIpAddress())
                .reason(entity.getReason())
                .build();
    }
}
