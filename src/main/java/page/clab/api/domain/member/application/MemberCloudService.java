package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.dao.MemberRepository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.global.auth.util.AuthUtil;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.dto.response.FileInfo;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.FileSystemUtil;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberCloudService {

    private final MemberRepository memberRepository;

    @Value("${resource.file.path}")
    private String filePath;

    @Transactional(readOnly = true)
    public PagedResponseDto<CloudUsageInfo> getAllCloudUsages(Pageable pageable) {
        Page<Member> members = memberRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(members.map(this::getCloudUsageForMember));
    }

    @Transactional(readOnly = true)
    public CloudUsageInfo getCloudUsageByMemberId(String memberId) throws PermissionDeniedException {
        Member currentMember = getCurrentMember();
        Member targetMember = validateMemberExistence(memberId);
        targetMember.validateAccessPermissionForCloud(currentMember);
        File directory = getMemberDirectory(targetMember.getId());
        long usage = FileSystemUtil.calculateDirectorySize(directory);
        return CloudUsageInfo.create(targetMember.getId(), usage);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<FileInfo> getFilesInMemberDirectory(String memberId, Pageable pageable) {
        validateMemberExistence(memberId);
        File directory = getMemberDirectory(memberId);
        List<File> files = FileSystemUtil.getFilesInDirectory(directory);
        return new PagedResponseDto<>(files.stream().map(FileInfo::toDto).toList(), pageable, files.size());
    }

    private CloudUsageInfo getCloudUsageForMember(Member member) {
        File directory = getMemberDirectory(member.getId());
        long usage = FileSystemUtil.calculateDirectorySize(directory);
        return CloudUsageInfo.create(member.getId(), usage);
    }

    private Member validateMemberExistence(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 없습니다."));
    }

    private File getMemberDirectory(String memberId) {
        return new File(filePath + "/members/" + memberId);
    }

    private Member getCurrentMember() {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 없습니다."));
    }

}
