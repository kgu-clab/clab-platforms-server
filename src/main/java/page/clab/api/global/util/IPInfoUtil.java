package page.clab.api.global.util;

import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import page.clab.api.global.common.dto.IPInfoResponse;
import page.clab.api.global.config.IPInfoConfig;

@Slf4j
@Component
public class IPInfoUtil {

    private static RestClient restClient;
    private static String accessToken;
    private static AttributeStrategy attributeStrategy;

    public IPInfoUtil(IPInfoConfig ipInfoConfig) {
        restClient = ipInfoConfig.getRestClient();
        accessToken = ipInfoConfig.getAccessToken();
        IPInfoUtil.attributeStrategy = ipInfoConfig.attributeStrategy();
    }

    public static IPInfoResponse getIpInfo(String ipAddress) {
        return restClient.get()
                .uri(ipAddress)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.warn("4xx error occurred while getting ip info. Status code: {}", response.getStatusCode());
                }))
                .body(IPInfoResponse.class);
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
