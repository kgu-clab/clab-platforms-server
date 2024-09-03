package page.clab.api.global.auth.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

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
            // YAML 파일 읽기/쓰기 옵션 설정
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setIndent(2);
            options.setPrettyFlow(true);

            Yaml yaml = new Yaml(options);
            Path path = Paths.get(whitelistPath);

            // 파일이 존재하지 않을 경우, 기본 YAML 파일 생성
            if (Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                Map<String, Map<String, List<String>>> defaultContent = Map.of(
                        "whitelist", Map.of(
                                "fixedIps", List.of(""),
                                "temporaryIps", List.of("*")
                        )
                );
                yaml.dump(defaultContent, Files.newBufferedWriter(path));
                log.info("Whitelist YAML file created at {}", whitelistPath);
            }

            // YAML 파일에서 데이터 읽기
            try (InputStream inputStream = Files.newInputStream(path)) {
                Map<String, Map<String, List<String>>> data = yaml.load(inputStream);
                Map<String, List<String>> whitelist = data.getOrDefault("whitelist", Map.of());

                List<String> fixedIps = whitelist.getOrDefault("fixedIps", List.of());
                List<String> temporaryIps = whitelist.getOrDefault("temporaryIps", List.of());

                return Stream.concat(
                        fixedIps.stream().filter(Objects::nonNull),
                        temporaryIps.stream().filter(Objects::nonNull)
                ).toList();
            }
        } catch (IOException e) {
            log.error("Failed to load or create IP whitelist from path: {}", whitelistPath, e);
            return List.of("*");
        }
    }
}
