package page.clab.api.global.auth.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WhitelistService {

    @Value("${security.whitelist.enabled}")
    private boolean whitelistEnabled;

    @Value("${security.whitelist.path}")
    private String whitelistPath;

    public List<String> loadWhitelistIps() {
        if (!whitelistEnabled) {
            return List.of("*");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Path path = Paths.get(whitelistPath);
            if (Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                Map<String, List<String>> defaultContent = Map.of("allowedIps", List.of("*"));
                mapper.writeValue(Files.newBufferedWriter(path), defaultContent);
                log.info("Whitelist IP file created at {}", whitelistPath);
            }
            Map<String, List<String>> data = mapper.readValue(path.toFile(), new TypeReference<>() {
            });
            return data.getOrDefault("allowedIps", List.of("*"));
        } catch (IOException e) {
            log.error("Failed to load or create IP whitelist from path: {}", whitelistPath, e);
            return List.of("*");
        }
    }
}
