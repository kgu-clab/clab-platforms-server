package page.clab.api.domain.auth.redisIpAccessMonitor.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.ClearIpAccessMonitorPort;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.RegisterIpAccessMonitorPort;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.RemoveIpAccessMonitorPort;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.RetrieveIpAccessMonitorPort;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;

@Component
@RequiredArgsConstructor
public class RedisIpAccessMonitorAdapter implements
    RegisterIpAccessMonitorPort,
    RetrieveIpAccessMonitorPort,
    RemoveIpAccessMonitorPort,
    ClearIpAccessMonitorPort {

    private final RedisIpAccessMonitorRepository repository;

    @Override
    public Optional<RedisIpAccessMonitor> findById(String ipAddress) {
        return repository.findById(ipAddress);
    }

    @Override
    public void deleteById(String ipAddress) {
        repository.findById(ipAddress)
            .ifPresent(repository::delete);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    public List<RedisIpAccessMonitor> findAll() {
        return StreamSupport
            .stream(repository.findAll().spliterator(), false)
            .toList();
    }

    @Override
    public void save(RedisIpAccessMonitor redisIpAccessMonitor) {
        repository.save(redisIpAccessMonitor);
    }
}
