package page.clab.api.domain.login.domain;

import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeoIpInfo {

    private String location;

    private String city;

    private String country;

    private Double latitude;

    private Double longitude;

    public static GeoIpInfo create(City city, Country country, Location location) {
        return GeoIpInfo.builder()
                .location(city.getName() + " " + country.getName())
                .city(city.getName())
                .country(country.getName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    public static GeoIpInfo createUnknown() {
        return GeoIpInfo.builder()
                .location("Unknown")
                .city(null)
                .country(null)
                .latitude(null)
                .longitude(null)
                .build();
    }

}
