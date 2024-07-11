package page.clab.api.domain.memberManagement.member.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.dto.response.FileInfo;

public interface RetrieveFilesInMemberDirectoryUseCase {
    PagedResponseDto<FileInfo> retrieveFilesInMemberDirectory(String memberId, Pageable pageable);
}
