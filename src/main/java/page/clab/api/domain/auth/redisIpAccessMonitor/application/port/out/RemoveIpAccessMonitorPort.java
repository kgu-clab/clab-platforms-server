package page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out;

public interface RemoveIpAccessMonitorPort {

    void deleteById(String ipAddress);
}
