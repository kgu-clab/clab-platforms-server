package page.clab.api.domain.login.application;

import page.clab.api.domain.login.domain.RedisToken;
import page.clab.api.domain.login.dto.response.TokenInfo;
import page.clab.api.domain.member.domain.Role;

import java.util.List;

public interface RedisTokenService {
    RedisToken getRedisTokenByAccessToken(String accessToken);
    RedisToken getRedisTokenByRefreshToken(String refreshToken);
    List<String> getCurrentLoggedInUsers();
    void saveRedisToken(String memberId, Role role, TokenInfo tokenInfo, String ip);
    void deleteRedisTokenByAccessToken(String accessToken);
    void deleteRedisTokenByMemberId(String memberId);
}