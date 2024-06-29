package page.clab.api.domain.member.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.FilesInMemberDirectoryRetrievalService;
import page.clab.api.domain.member.dao.MemberRepository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.dto.response.FileInfo;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.util.FileSystemUtil;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilesInMemberDirectoryRetrievalServiceImpl implements FilesInMemberDirectoryRetrievalService {

    private final MemberRepository memberRepository;

    @Value("${resource.file.path}")
    private String filePath;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<FileInfo> retrieve(String memberId, Pageable pageable) {
        validateMemberExistence(memberId);
        File directory = getMemberDirectory(memberId);
        List<File> files = FileSystemUtil.getFilesInDirectory(directory);
        return new PagedResponseDto<>(files.stream().map(FileInfo::toDto).toList(), pageable, files.size());
    }

    private Member validateMemberExistence(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 없습니다."));
    }

    private File getMemberDirectory(String memberId) {
        return new File(filePath + "/members/" + memberId);
    }
}
