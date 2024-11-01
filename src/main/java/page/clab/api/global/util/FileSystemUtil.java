package page.clab.api.global.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@code FileSystemUtil}은 파일 시스템 관련 작업을 지원하는 유틸리티 클래스입니다.
 * 디렉토리의 크기를 계산하고, 디렉토리에 포함된 파일 목록을 반환하며, 크기 문자열을 바이트 단위로 변환하는 기능을 제공합니다.
 *
 * <p>주요 기능:
 * <ul>
 *     <li>{@link #calculateDirectorySize(File)} - 디렉토리의 총 크기를 계산합니다.</li>
 *     <li>{@link #getFilesInDirectory(File)} - 디렉토리 내 모든 파일 목록을 반환합니다.</li>
 *     <li>{@link #convertToBytes(String)} - 주어진 크기 문자열을 바이트 단위로 변환합니다.</li>
 * </ul>
 */
public class FileSystemUtil {

    public static long calculateDirectorySize(File directory) {
        long bytes = 0;
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return -1;
            }
        }
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        bytes += file.length();
                    } else if (file.isDirectory()) {
                        bytes += calculateDirectorySize(file);
                    }
                }
            }
        } else {
            return -1;
        }
        return bytes;
    }

    public static List<File> getFilesInDirectory(File directory) {
        List<File> fileList = new ArrayList<>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                fileList.addAll(Arrays.asList(files));
            }
        }
        return fileList;
    }

    public static long convertToBytes(String size) {
        String number = size.replaceAll("[^\\d.]", "");
        String unit = size.replaceAll("[\\d.]", "").toUpperCase();
        long sizeInBytes = Long.parseLong(number);
        switch (unit) {
            case "KB":
                sizeInBytes *= 1024;
                break;
            case "MB":
                sizeInBytes *= 1024 * 1024;
                break;
            case "GB":
                sizeInBytes *= 1024 * 1024 * 1024;
                break;
            default:
                throw new IllegalArgumentException("Invalid size unit, only KB, MB, and GB are allowed.");
        }
        return sizeInBytes;
    }
}
