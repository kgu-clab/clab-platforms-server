package page.clab.api.global.common.file.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.util.Date;

@Getter
@Builder
public class FileInfo {

    private String fileName;
    private Long fileSizeInBytes;
    private Date creationDate;
    private Date modificationDate;

    public static FileInfo toDto(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }

        return FileInfo.builder()
                .fileName(file.getName())
                .fileSizeInBytes(file.length())
                .creationDate(new Date(file.lastModified()))
                .modificationDate(new Date(file.lastModified()))
                .build();
    }
}
