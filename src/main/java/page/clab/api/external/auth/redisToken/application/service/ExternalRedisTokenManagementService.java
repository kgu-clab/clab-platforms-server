package page.clab.api.external.auth.redisToken.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.auth.login.application.port.in.ManageRedisTokenUseCase;
import page.clab.api.domain.auth.login.domain.RedisToken;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.external.auth.redisToken.application.port.ExternalManageRedisTokenUseCase;

@Service
@RequiredArgsConstructor
public class ExternalRedisTokenManagementService implements ExternalManageRedisTokenUseCase {

    private final ManageRedisTokenUseCase manageRedisTokenUseCase;

    @Override
    public RedisToken findByAccessToken(String accessToken) {
        return manageRedisTokenUseCase.findByAccessToken(accessToken);
    }

    @Override
    public RedisToken findByRefreshToken(String refreshToken) {
        return manageRedisTokenUseCase.findByRefreshToken(refreshToken);
    }

    @Override
    public void saveToken(String memberId, Role role, TokenInfo tokenInfo, String ip) {
        manageRedisTokenUseCase.saveToken(memberId, role, tokenInfo, ip);
    }

    @Override
    public void deleteByAccessToken(String accessToken) {
        manageRedisTokenUseCase.deleteByAccessToken(accessToken);
    }

    @Override
    public void deleteByMemberId(String memberId) {
        manageRedisTokenUseCase.deleteByMemberId(memberId);
    }
}
