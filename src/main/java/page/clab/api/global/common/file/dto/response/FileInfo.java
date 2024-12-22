package page.clab.api.global.common.file.dto.response;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileInfo {

    private String fileName;
    private Long fileSizeInBytes;
    private Date creationDate;
    private Date modificationDate;
}
