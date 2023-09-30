package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.BlacklistIpRepository;
import page.clab.api.type.entity.BlacklistIp;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlacklistService {

    private final BlacklistIpRepository blacklistIpRepository;

    private final UserService userService;

    public void addBlacklistedIp(String ipAddress) throws PermissionDeniedException {
        userService.checkUserAdminRole();
        if (!isBlacklistedIp(ipAddress)) {
            BlacklistIp blacklistIp = BlacklistIp.builder().ipAddress(ipAddress).build();
            blacklistIpRepository.save(blacklistIp);
            log.info("IP address {} added to the blacklist", ipAddress);
        } else {
            log.info("IP address {} is already blacklisted", ipAddress);
        }
    }

    public List<BlacklistIp> getBlacklistedIps() throws PermissionDeniedException {
        userService.checkUserAdminRole();
        List<BlacklistIp> blacklistedIps = blacklistIpRepository.findAll();
        return blacklistedIps;
    }

    @Transactional
    public void deleteBlacklistedIp(String ipAddress) throws PermissionDeniedException {
        userService.checkUserAdminRole();
        if (isBlacklistedIp(ipAddress)) {
            blacklistIpRepository.deleteByIpAddress(ipAddress);
            log.info("IP address {} removed from the blacklist", ipAddress);
        } else {
            log.info("IP address {} is not blacklisted", ipAddress);
        }
    }

    public void clearBlacklist() throws PermissionDeniedException {
        userService.checkUserAdminRole();
        blacklistIpRepository.deleteAll();
        log.info("Blacklist cleared");
    }

    public boolean isBlacklistedIp(String ipAddress) {
        return blacklistIpRepository.existsByIpAddress(ipAddress);
    }

}


