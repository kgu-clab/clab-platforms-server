package page.clab.api.domain.blacklistIp.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.blacklistIp.application.port.out.RegisterBlacklistIpPort;
import page.clab.api.domain.blacklistIp.application.port.out.RemoveBlacklistIpPort;
import page.clab.api.domain.blacklistIp.application.port.out.RetrieveBlacklistIpPort;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BlacklistIpPersistenceAdapter implements
        RegisterBlacklistIpPort,
        RetrieveBlacklistIpPort,
        RemoveBlacklistIpPort {

    private final BlacklistIpRepository blacklistIpRepository;

    @Override
    public BlacklistIp save(BlacklistIp blacklistIp) {
        return blacklistIpRepository.save(blacklistIp);
    }

    @Override
    public Optional<BlacklistIp> findByIpAddress(String ipAddress) {
        return blacklistIpRepository.findByIpAddress(ipAddress);
    }

    @Override
    public BlacklistIp findByIpAddressOrThrow(String ipAddress) {
        return blacklistIpRepository.findByIpAddress(ipAddress)
                .orElseThrow(() -> new NotFoundException("[BlacklistIp] IP: " + ipAddress + "에 해당하는 블랙리스트 IP가 존재하지 않습니다."));
    }

    @Override
    public void delete(BlacklistIp blacklistIp) {
        blacklistIpRepository.delete(blacklistIp);
    }

    @Override
    public void deleteAll() {
        blacklistIpRepository.deleteAll();
    }

    @Override
    public List<BlacklistIp> findAll() {
        return blacklistIpRepository.findAll();
    }

    @Override
    public Page<BlacklistIp> findAll(Pageable pageable) {
        return blacklistIpRepository.findAll(pageable);
    }
}
