package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.BlacklistIpRepository;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.BlacklistIp;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlacklistIpService {

    private final BlacklistIpRepository blacklistIpRepository;

    private final MemberService memberService;

    public Long addBlacklistedIp(String ipAddress) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        BlacklistIp blacklistIp = getBlacklistIpByIpAddress(ipAddress);
        if (blacklistIp != null) {
            return blacklistIp.getId();
        }
        blacklistIp = BlacklistIp.builder().ipAddress(ipAddress).build();
        return blacklistIpRepository.save(blacklistIp).getId();
    }

    public PagedResponseDto<BlacklistIp> getBlacklistedIps(Pageable pageable) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Page<BlacklistIp> blacklistedIps = blacklistIpRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(blacklistedIps);
    }

    @Transactional
    public Long deleteBlacklistedIp(String ipAddress) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        BlacklistIp blacklistIp = getBlacklistIpByIpAddressOrThrow(ipAddress);
        blacklistIpRepository.delete(blacklistIp);
        return blacklistIp.getId();
    }

    public void clearBlacklist() throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        blacklistIpRepository.deleteAll();
        log.info("서비스 접근 제한 IP 목록을 초기화하였습니다.");
    }

    private BlacklistIp getBlacklistIpByIpAddressOrThrow(String ipAddress) {
        return blacklistIpRepository.findByIpAddress(ipAddress).
                orElseThrow(() -> new NotFoundException("해당 IP 주소를 찾을 수 없습니다."));
    }

    private BlacklistIp getBlacklistIpByIpAddress(String ipAddress) {
        return blacklistIpRepository.findByIpAddress(ipAddress)
                .orElse(null);
    }

}


