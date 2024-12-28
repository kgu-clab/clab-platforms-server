package page.clab.api.global.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;

/**
 * {@code TempFileUtil} 클래스는 보안 설정이 적용된 임시 파일을 생성하는 유틸리티 클래스입니다.
 * <p>
 * 임시 파일은 POSIX 파일 시스템(예: UNIX, 리눅스 계열)에서 파일 소유자만 접근할 수 있도록 권한을 설정하여 보안을 강화합니다.
 */
public class TempFileUtil {

    /**
     * 보안 설정을 적용한 임시 파일을 생성합니다.
     *
     * @param prefix 파일 이름의 접두사
     * @param suffix 파일 이름의 접미사 (확장자)
     * @return 생성된 임시 파일의 경로
     * @throws IOException 파일 생성 중 오류가 발생한 경우
     */
    public static Path createSecureTempFile(String prefix, String suffix) throws IOException {
        Path tempFilePath = Files.createTempFile(prefix, suffix);
        // POSIX 파일 시스템(UNIX, 리눅스 계열)인 경우 파일 소유자만 읽을 수 있도록 권한 설정
        // 임시 파일에 대한 접근이 제한되어 다른 로컬 사용자가 파일에 접근하거나 파일 내용을 노출할 위험을 줄일 수 있음
        if (tempFilePath.getFileSystem().supportedFileAttributeViews().contains("posix")) {
            EnumSet<PosixFilePermission> permissions = EnumSet.of(
                PosixFilePermission.OWNER_READ
            );
            Files.setPosixFilePermissions(tempFilePath, permissions);
        }
        return tempFilePath;
    }
}
