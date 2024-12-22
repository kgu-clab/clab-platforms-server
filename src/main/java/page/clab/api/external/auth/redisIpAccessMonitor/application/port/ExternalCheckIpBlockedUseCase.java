package page.clab.api.external.auth.redisIpAccessMonitor.application.port;

public interface ExternalCheckIpBlockedUseCase {

    boolean isIpBlocked(String clientIpAddress);
}
