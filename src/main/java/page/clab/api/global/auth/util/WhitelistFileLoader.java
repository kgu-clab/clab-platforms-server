package page.clab.api.global.auth.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class WhitelistFileLoader {

    @Value("${security.whitelist.path}")
    private String whitelistPath;

    private final Yaml yaml;
    private final Lock fileLock = new ReentrantLock();

    /**
     * 화이트리스트 YAML 파일에서 IP 목록을 로드합니다.
     * @return 화이트리스트에 포함된 IP 목록
     */
    public List<String> loadWhitelistIps() {
        fileLock.lock();
        try {
            Path path = Paths.get(whitelistPath);

            if (Files.notExists(path)) {
                createDefaultWhitelistFile(path, yaml);
            }

            return parseWhitelistFile(yaml, path);
        } catch (IOException e) {
            log.error("Failed to load or create IP whitelist", e);
            return List.of();
        } finally {
            fileLock.unlock();
        }
    }

    /**
     * 기본 화이트리스트 YAML 파일을 생성합니다.
     * @param path 파일 경로
     * @param yaml Yaml 객체
     * @throws IOException 파일 생성 중 오류 발생 시
     */
    private void createDefaultWhitelistFile(Path path, Yaml yaml) throws IOException {
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

    /**
     * YAML 파일을 파싱하여 화이트리스트 IP 목록을 반환합니다.
     * @param yaml Yaml 객체
     * @param path YAML 파일 경로
     * @return 화이트리스트에 포함된 IP 목록
     * @throws IOException 파일 읽기 중 오류 발생 시
     */
    private List<String> parseWhitelistFile(Yaml yaml, Path path) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path)) {
            Map<String, Map<String, List<String>>> data = yaml.load(inputStream);

            if (CollectionUtils.isEmpty(data)) {
                log.warn("YAML file is empty or invalid");
                return List.of();
            }

            Map<String, List<String>> whitelist = data.getOrDefault("whitelist", Map.of());

            List<String> fixedIps = whitelist.getOrDefault("fixedIps", List.of());
            List<String> temporaryIps = whitelist.getOrDefault("temporaryIps", List.of());

            return Stream.concat(
                    fixedIps != null ? fixedIps.stream() : Stream.empty(),
                    temporaryIps != null ? temporaryIps.stream() : Stream.empty()
            ).toList();
        } catch (Exception e) {
            log.error("Failed to parse IP whitelist", e);
            return List.of();
        }
    }
}
