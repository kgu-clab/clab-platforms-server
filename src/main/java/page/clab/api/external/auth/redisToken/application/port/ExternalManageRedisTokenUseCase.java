package page.clab.api.external.auth.redisToken.application.port;

import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.auth.login.domain.RedisToken;
import page.clab.api.domain.memberManagement.member.domain.Role;

public interface ExternalManageRedisTokenUseCase {

    RedisToken findByAccessToken(String accessToken);

    RedisToken findByRefreshToken(String refreshToken);

    void saveToken(String memberId, Role role, TokenInfo tokenInfo, String ip);

    void deleteByAccessToken(String accessToken);

    void deleteByMemberId(String memberId);
}
