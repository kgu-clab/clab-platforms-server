package page.clab.api.global.util;

import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import page.clab.api.global.common.dto.IpInfoResponse;
import page.clab.api.global.config.IPInfoConfig;

@Slf4j
@Component
public class IpInfoUtil {

    private static final RestClient restClient = RestClient.create();

    private static final String IPINFO_BASE_URL = "https://ipinfo.io/";

    private static String accessToken;

    private static AttributeStrategy attributeStrategy;

    public IpInfoUtil(IPInfoConfig ipInfoConfig) {
        accessToken = ipInfoConfig.getAccessToken();
        IpInfoUtil.attributeStrategy = ipInfoConfig.attributeStrategy();
    }

    public static IpInfoResponse getIpInfo(String ipAddress) {
        return restClient.get()
                .uri(IPINFO_BASE_URL + ipAddress + "?token=" + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.warn("4xx error occurred while getting ip info. Status code: {}", response.getStatusCode());
                }))
                .body(IpInfoResponse.class);
    }

    public static IPResponse getIpInfo(HttpServletRequest request) {
        IPResponse ipResponse = attributeStrategy.getAttribute(request);
        if (ipResponse == null) {
            log.warn("Failed to get geolocation information from IPInfo. IPResponse is null.");
            return null;
        }
        return ipResponse;
    }

}
