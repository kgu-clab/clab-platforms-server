package page.clab.api.external.auth.redisToken.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.auth.login.application.port.in.ManageRedisTokenUseCase;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.external.auth.redisToken.application.port.ExternalManageRedisTokenUseCase;

@Service
@RequiredArgsConstructor
public class ExternalRedisTokenManagementService implements ExternalManageRedisTokenUseCase {

    private final ManageRedisTokenUseCase manageRedisTokenUseCase;

    @Override
    public void deleteByMemberId(String memberId) {
        manageRedisTokenUseCase.deleteByMemberId(memberId);
    }

    @Override
    public void saveToken(String memberId, Role role, TokenInfo tokenInfo, String clientIpAddress) {
        manageRedisTokenUseCase.saveToken(memberId, role, tokenInfo, clientIpAddress);
    }
}
