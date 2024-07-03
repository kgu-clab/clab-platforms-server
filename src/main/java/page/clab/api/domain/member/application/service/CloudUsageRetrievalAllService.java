package page.clab.api.domain.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveAllCloudUsageUseCase;
import page.clab.api.domain.member.application.port.out.LoadMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.FileSystemUtil;

import java.io.File;

@Service
@RequiredArgsConstructor
public class CloudUsageRetrievalAllService implements RetrieveAllCloudUsageUseCase {

    private final LoadMemberPort loadMemberPort;

    @Value("${resource.file.path}")
    private String filePath;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<CloudUsageInfo> retrieve(Pageable pageable) {
        Page<Member> members = loadMemberPort.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(members.map(this::getCloudUsageForMember));
    }

    private CloudUsageInfo getCloudUsageForMember(Member member) {
        File directory = getMemberDirectory(member.getId());
        long usage = FileSystemUtil.calculateDirectorySize(directory);
        return CloudUsageInfo.create(member.getId(), usage);
    }

    private File getMemberDirectory(String memberId) {
        return new File(filePath + "/members/" + memberId);
    }
}
