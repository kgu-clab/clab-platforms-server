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

/**
 * {@code IPInfoUtil} 클래스는 IP 주소에 대한 정보를 가져오기 위한 유틸리티 메서드를 제공합니다.
 * IP 정보를 가져오기 위해 IPInfo API를 호출하고, IP 기반 위치 데이터를 요청으로부터 가져옵니다.
 */
@Component
@Slf4j
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
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) ->
                    log.warn("4xx error occurred while getting ip info. Status code: {}", response.getStatusCode())))
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
