package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileInfo {

    private String fileName;

    private Long fileSizeInBytes;

    private Date creationDate;

    private Date modificationDate;

    public static FileInfo of(File file) {
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
