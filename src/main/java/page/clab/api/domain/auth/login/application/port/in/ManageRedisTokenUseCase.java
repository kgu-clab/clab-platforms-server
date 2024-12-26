package page.clab.api.domain.auth.login.application.port.in;

import java.util.List;
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.auth.login.domain.RedisToken;
import page.clab.api.domain.memberManagement.member.domain.Role;

public interface ManageRedisTokenUseCase {

    RedisToken findByAccessToken(String accessToken);

    RedisToken findByRefreshToken(String refreshToken);

    List<String> getCurrentLoggedInUsers();

    void saveToken(String memberId, Role role, TokenInfo tokenInfo, String ip);

    void deleteByAccessToken(String accessToken);
}
