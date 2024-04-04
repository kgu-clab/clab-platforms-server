package page.clab.api.global.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import page.clab.api.domain.login.domain.GeoIpInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

@Component
public class GeoIpUtil {

    private static DatabaseReader databaseReader;

    public GeoIpUtil(ResourceLoader resourceLoader, @Value("${geoip2.database.path}") String databasePath) throws IOException {
        try (InputStream inputStream = resourceLoader.getResource(databasePath).getInputStream()) {
            databaseReader = new DatabaseReader.Builder(inputStream).build();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing GeoIpUtil", e);
        }
    }

    public static GeoIpInfo getInfoByIp(String ipAddress) {
        try {
            InetAddress ip = InetAddress.getByName(ipAddress);
            CityResponse response = databaseReader.city(ip);
            City city = response.getCity();
            Country country = response.getCountry();
            Location location = response.getLocation();
            return GeoIpInfo.create(city, country, location);
        } catch (IOException | GeoIp2Exception e) {
            return GeoIpInfo.createUnknown();
        }
    }

}