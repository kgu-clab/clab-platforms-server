package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.dao.MemberRepository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.dto.response.FileInfo;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.FileSystemUtil;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberCloudService {

    private final MemberRepository memberRepository;

    @Value("${resource.file.path}")
    private String filePath;

    public PagedResponseDto<CloudUsageInfo> getAllCloudUsages(Pageable pageable) {
        Page<Member> members = memberRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(members.map(member -> {
            try {
                return getCloudUsageByMemberId(member.getId());
            } catch (PermissionDeniedException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public CloudUsageInfo getCloudUsageByMemberId(String memberId) throws PermissionDeniedException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 없습니다."));
        File directory = new File(filePath + "/members/" + memberId);
        long usage = FileSystemUtil.calculateDirectorySize(directory);
        if (usage == -1) {
            throw new NotFoundException("올바르지 않은 접근입니다.");
        }
        return CloudUsageInfo.builder()
                .memberId(memberId)
                .usage(usage)
                .build();
    }

    public PagedResponseDto<FileInfo> getFilesInMemberDirectory(String memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 없습니다."));
        File directory = new File(filePath + "/members/" + memberId);
        File[] files = FileSystemUtil.getFilesInDirectory(directory).toArray(new File[0]);
        if (files.length == 0) {
            return null;
        }
        int totalFiles = files.length;
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalFiles);
        if (start >= totalFiles) {
            return null;
        }
        Page<FileInfo> fileInfoPage = new PageImpl<>(Arrays.stream(files)
                .map(FileInfo::of)
                .collect(Collectors.toList()).subList(start, end), pageable, totalFiles);
        return new PagedResponseDto<>(fileInfoPage);
    }

}
