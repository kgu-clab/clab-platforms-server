package page.clab.api.external.auth.blacklistIp.application.port;

public interface ExternalRetrieveBlacklistIpUseCase {

    boolean existsByIpAddress(String clientIpAddress);
}
