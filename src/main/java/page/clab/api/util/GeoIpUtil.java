package page.clab.api.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import page.clab.api.type.dto.GeoIpInfo;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@Component
public class GeoIpUtil {

    private static DatabaseReader databaseReader;

    public GeoIpUtil(@Value("${geoip2.database.path}") String databasePath) throws IOException {
        File database = new File(databasePath);
        databaseReader = new DatabaseReader.Builder(database).build();
    }

    public static GeoIpInfo getInfoByIp(String ipAddress) {
        GeoIpInfo geoIpInfo = new GeoIpInfo();

        try {
            InetAddress ip = InetAddress.getByName(ipAddress);
            CityResponse response = databaseReader.city(ip);

            City city = response.getCity();
            Country country = response.getCountry();
            Location location = response.getLocation();

            geoIpInfo.setCity(city.getName());
            geoIpInfo.setCountry(country.getName());
            geoIpInfo.setLatitude(location.getLatitude());
            geoIpInfo.setLongitude(location.getLongitude());

        } catch (IOException | GeoIp2Exception e) {
            throw new RuntimeException(e);
        }

        return geoIpInfo;
    }
}