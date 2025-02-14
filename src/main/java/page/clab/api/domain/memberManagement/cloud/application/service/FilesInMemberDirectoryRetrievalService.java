package page.clab.api.domain.memberManagement.cloud.application.service;

import java.io.File;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.cloud.application.port.in.RetrieveFilesInMemberDirectoryUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.dto.mapper.FileDtoMapper;
import page.clab.api.global.common.file.dto.response.FileInfo;
import page.clab.api.global.util.FileSystemUtil;

@Service
@RequiredArgsConstructor
public class FilesInMemberDirectoryRetrievalService implements RetrieveFilesInMemberDirectoryUseCase {

    private final RetrieveMemberPort retrieveMemberPort;
    private final FileDtoMapper mapper;

    @Value("${resource.file.path}")
    private String filePath;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<FileInfo> retrieveFilesInMemberDirectory(String memberId, Pageable pageable) {
        Member member = retrieveMemberPort.getById(memberId);
        File directory = getMemberDirectory(member.getId());
        List<File> files = FileSystemUtil.getFilesInDirectory(directory);
        return new PagedResponseDto<>(files.stream().map(mapper::create).toList(), pageable, files.size());
    }

    private File getMemberDirectory(String memberId) {
        return new File(filePath + "/members/" + memberId);
    }
}
