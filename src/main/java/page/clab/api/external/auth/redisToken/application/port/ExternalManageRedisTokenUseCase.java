package page.clab.api.external.auth.redisToken.application.port;

import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.memberManagement.member.domain.Role;

public interface ExternalManageRedisTokenUseCase {

    void deleteByMemberId(String memberId);

    void saveToken(String memberId, Role role, TokenInfo tokenInfo, String clientIpAddress);
}
