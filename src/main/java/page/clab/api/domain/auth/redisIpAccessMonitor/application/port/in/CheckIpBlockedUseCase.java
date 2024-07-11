package page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in;

public interface CheckIpBlockedUseCase {
    boolean isIpBlocked(String ipAddress);
}
