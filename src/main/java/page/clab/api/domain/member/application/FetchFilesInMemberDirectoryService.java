package page.clab.api.domain.member.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.dto.response.FileInfo;

public interface FetchFilesInMemberDirectoryService {
    PagedResponseDto<FileInfo> execute(String memberId, Pageable pageable);
}