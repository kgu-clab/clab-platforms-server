package page.clab.api.domain.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveFilesInMemberDirectoryUseCase;
import page.clab.api.domain.member.application.port.out.LoadMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.dto.response.FileInfo;
import page.clab.api.global.util.FileSystemUtil;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilesInMemberDirectoryRetrievalService implements RetrieveFilesInMemberDirectoryUseCase {

    private final LoadMemberPort loadMemberPort;

    @Value("${resource.file.path}")
    private String filePath;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<FileInfo> retrieve(String memberId, Pageable pageable) {
        Member member = loadMemberPort.findByIdOrThrow(memberId);
        File directory = getMemberDirectory(member.getId());
        List<File> files = FileSystemUtil.getFilesInDirectory(directory);
        return new PagedResponseDto<>(files.stream().map(FileInfo::toDto).toList(), pageable, files.size());
    }

    private File getMemberDirectory(String memberId) {
        return new File(filePath + "/members/" + memberId);
    }
}
