package page.clab.api.global.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import page.clab.api.global.common.file.exception.DirectoryCreationException;
import page.clab.api.global.common.file.exception.FilePermissionException;
import page.clab.api.global.common.file.exception.InvalidFileAttributeException;

/**
 * {@code FileUtil}은 파일 및 디렉토리와 관련된 유틸리티 기능을 제공합니다. 파일 경로 검증, 파일 권한 설정, 고유 파일명 생성, 디렉토리 생성 등의 기능을 수행합니다.
 *
 * <p>주요 기능:
 * <ul>
 *     <li>{@link #validateFilePath(String, String)} - 파일 경로가 기본 디렉토리 내에 있는지 확인합니다.</li>
 *     <li>{@link #validateFileExists(Path)} - 파일 존재 여부를 검증합니다.</li>
 *     <li>{@link #makeFileName(String)} - 고유한 파일명을 생성합니다.</li>
 *     <li>{@link #ensureParentDirectoryExists(File, String)} - 디렉토리가 존재하지 않으면 생성합니다.</li>
 *     <li>{@link #validateFileAttributes(String, Set)} - 파일명과 확장자를 검증합니다.</li>
 *     <li>{@link #setFilePermissions(File, String, String)} - 파일의 읽기 전용 권한을 설정합니다.</li>
 * </ul>
 */
public class FileUtil {

    /**
     * 주어진 파일 경로가 기본 디렉토리 내에 포함되는지 확인하고, 정상적인 경로를 반환합니다.
     *
     * @param filePath      검증할 파일 경로
     * @param baseDirectory 기본 디렉토리
     * @return 정상적인 파일 경로일 경우 Path 객체를 반환
     * @throws InvalidPathException 잘못된 경로일 경우 발생
     */
    public static Path validateFilePath(String filePath, String baseDirectory) throws InvalidPathException {
        Path baseDir = Paths.get(baseDirectory).normalize().toAbsolutePath();
        Path resolvedPath = baseDir.resolve(filePath).normalize();

        // 경로가 기본 디렉토리 내부에 있는지 확인
        if (!resolvedPath.startsWith(baseDir)) {
            throw new InvalidPathException(filePath, "Invalid file path: Path traversal detected.");
        }
        return resolvedPath;
    }

    /**
     * 파일이 존재하는지 확인합니다. 파일이 존재하지 않으면 예외를 발생시킵니다.
     *
     * @param filePath 검증할 파일 경로
     * @throws InvalidPathException 파일이 존재하지 않는 경우 발생
     */
    public static void validateFileExists(Path filePath) throws InvalidPathException {
        if (!filePath.toFile().exists()) {
            throw new InvalidPathException(filePath.toString(), "File does not exist: " + filePath);
        }
    }

    /**
     * 고유한 파일명을 생성합니다.
     *
     * @param extension 파일 확장자
     * @return 생성된 고유 파일명
     */
    public static String makeFileName(String extension) {
        return System.nanoTime() + "_" + UUID.randomUUID() + "." + extension;
    }

    /**
     * 디렉토리가 존재하지 않으면 생성합니다.
     *
     * @param file 디렉토리가 포함된 파일 객체
     */
    public static void ensureParentDirectoryExists(File file, String baseDirectory) {
        Path safePath = FileUtil.validateFilePath(file.getPath(), baseDirectory);

        File parentDir = safePath.getParent().toFile();
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new DirectoryCreationException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }
    }

    /**
     * 파일명과 확장자를 검증합니다.
     *
     * @param originalFilename   파일명
     * @param disallowExtensions 허용되지 않은 확장자 목록
     * @throws IllegalArgumentException 유효하지 않은 파일명이나 확장자일 경우 발생
     */
    public static void validateFileAttributes(String originalFilename, Set<String> disallowExtensions)
        throws IllegalArgumentException {
        String extension = FilenameUtils.getExtension(originalFilename);
        if (!validateFilename(originalFilename)) {
            throw new InvalidFileAttributeException("Invalid file name: " + originalFilename);
        }
        if (!validateExtension(extension, disallowExtensions)) {
            throw new InvalidFileAttributeException("Invalid file extension: " + extension);
        }
    }

    /**
     * 파일명이 유효한지 확인합니다.
     *
     * @param fileName 파일명
     * @return 유효한 파일명일 경우 true, 그렇지 않을 경우 false
     */
    public static boolean validateFilename(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        return !fileName.contains("..") && !fileName.contains("/") && !fileName.contains("\\");
    }

    /**
     * 확장자가 허용된 것인지 확인합니다.
     *
     * @param extension          파일 확장자
     * @param disallowExtensions 허용되지 않은 확장자 목록
     * @return 허용된 확장자일 경우 true, 그렇지 않을 경우 false
     */
    public static boolean validateExtension(String extension, Set<String> disallowExtensions) {
        return !disallowExtensions.contains(extension.toLowerCase());
    }

    /**
     * 파일의 읽기 전용 권한을 설정합니다. OS에 따라 적절한 권한을 설정합니다.
     *
     * @param file     파일 객체
     * @param savePath 파일 경로
     * @throws FilePermissionException 파일 권한 설정에 실패한 경우 발생
     */
    public static void setFilePermissions(File file, String savePath, String baseDirectory) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                setReadOnlyPermissionsWindows(file, savePath, baseDirectory);
            } else {
                FileUtil.setReadOnlyPermissionsUnix(savePath, baseDirectory);
            }
        } catch (Exception e) {
            throw new FilePermissionException(
                "Failed to set file permissions: " + LogSanitizerUtil.sanitizeForLog(savePath));
        }
    }

    /**
     * 윈도우 시스템에서 파일을 읽기 전용으로 설정합니다.
     *
     * @param file     파일 객체
     * @param savePath 파일 경로
     * @throws FilePermissionException 파일 권한 설정에 실패한 경우 발생
     */
    public static void setReadOnlyPermissionsWindows(File file, String savePath, String baseDirectory) {
        FileUtil.validateFilePath(file.getPath(), baseDirectory);
        if (!file.setReadOnly()) {
            throw new FilePermissionException(
                "Failed to set file read-only: " + LogSanitizerUtil.sanitizeForLog(savePath));
        }
    }

    /**
     * POSIX 파일 시스템에서 파일 소유자만 읽을 수 있도록 파일 권한을 설정합니다.
     *
     * @param filePath 파일 경로
     * @throws IOException 권한 설정 실패 시 발생
     */
    public static void setReadOnlyPermissionsUnix(String filePath, String baseDirectory) throws IOException {
        FileUtil.validateFilePath(filePath, baseDirectory);
        Path path = Paths.get(filePath);

        // POSIX 파일 권한을 소유자에게만 읽기 권한을 부여하도록 설정
        Set<PosixFilePermission> permissions = EnumSet.of(
            PosixFilePermission.OWNER_READ
        );
        Files.setPosixFilePermissions(path, permissions);
    }
}
