package page.clab.api.domain.login.application.port.in;

import page.clab.api.domain.login.domain.RedisToken;
import page.clab.api.domain.login.dto.response.TokenInfo;
import page.clab.api.domain.member.domain.Role;

import java.util.List;

public interface RedisTokenManagementUseCase {
    RedisToken findByAccessToken(String accessToken);
    RedisToken findByRefreshToken(String refreshToken);
    List<String> getCurrentLoggedInUsers();
    void saveToken(String memberId, Role role, TokenInfo tokenInfo, String ip);
    void deleteByAccessToken(String accessToken);
    void deleteByMemberId(String memberId);
}
