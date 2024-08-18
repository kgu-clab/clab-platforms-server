package page.clab.api.external.auth.redisToken.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.auth.login.application.port.in.ManageRedisTokenUseCase;
import page.clab.api.domain.auth.login.application.port.out.RemoveRedisTokenPort;
import page.clab.api.domain.auth.login.domain.RedisToken;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.external.auth.redisToken.application.port.ExternalManageRedisTokenUseCase;

@Service
@RequiredArgsConstructor
public class ExternalRedisTokenManagementService implements ExternalManageRedisTokenUseCase {

    private final ManageRedisTokenUseCase manageRedisTokenUseCase;
    private final RemoveRedisTokenPort removeRedisTokenPort;

    @Transactional(readOnly = true)
    @Override
    public RedisToken findByAccessToken(String accessToken) {
        return manageRedisTokenUseCase.findByAccessToken(accessToken);
    }

    @Transactional(readOnly = true)
    @Override
    public RedisToken findByRefreshToken(String refreshToken) {
        return manageRedisTokenUseCase.findByRefreshToken(refreshToken);
    }

    @Transactional
    @Override
    public void saveToken(String memberId, Role role, TokenInfo tokenInfo, String ip) {
        manageRedisTokenUseCase.saveToken(memberId, role, tokenInfo, ip);
    }

    @Transactional
    @Override
    public void deleteByAccessToken(String accessToken) {
        manageRedisTokenUseCase.deleteByAccessToken(accessToken);
    }

    @Transactional
    @Override
    public void deleteByMemberId(String memberId) {
        removeRedisTokenPort.deleteByMemberId(memberId);
    }
}
