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
                for (File file : files) {
                    fileList.add(file);
                }
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
