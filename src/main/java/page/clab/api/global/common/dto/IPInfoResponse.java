package page.clab.api.global.common.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IPInfoResponse {

    private String ip;
    private String city;
    private String region;
    private String country;
    private String loc;
    private String org;
    private String postal;
    private String timezone;
}
