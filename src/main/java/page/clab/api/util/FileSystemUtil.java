package page.clab.api.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileSystemUtil {

    public static long calculateDirectorySize(File directory) {
        long bytes = 0;
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return -1; // 디렉토리 생성 실패
            }
        }
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        bytes += file.length(); // 파일 크기 합산
                    } else if (file.isDirectory()) {
                        bytes += calculateDirectorySize(file);
                    }
                }
            }
        } else {
            return -1; // 디렉토리가 아닌 경우 -1 반환
        }
        return bytes;
    }

    public static List<File> getFilesInDirectory(File directory) {
        List<File> fileList = new ArrayList<>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

}
