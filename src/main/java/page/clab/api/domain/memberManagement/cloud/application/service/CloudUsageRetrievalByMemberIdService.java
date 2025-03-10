package page.clab.api.domain.memberManagement.cloud.application.service;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.cloud.application.dto.mapper.CloudDtoMapper;
import page.clab.api.domain.memberManagement.cloud.application.dto.response.CloudUsageInfo;
import page.clab.api.domain.memberManagement.cloud.application.port.in.RetrieveCloudUsageByMemberIdUseCase;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.util.FileSystemUtil;

@Service
@RequiredArgsConstructor
public class CloudUsageRetrievalByMemberIdService implements RetrieveCloudUsageByMemberIdUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveMemberPort retrieveMemberPort;
    private final CloudDtoMapper mapper;

    @Value("${resource.file.path}")
    private String filePath;

    @Override
    @Transactional(readOnly = true)
    public CloudUsageInfo retrieveCloudUsage(String memberId) {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        Member targetMember = retrieveMemberPort.getById(memberId);
        targetMember.validateAccessPermissionForCloud(currentMember);
        File directory = getMemberDirectory(targetMember.getId());
        long usage = FileSystemUtil.calculateDirectorySize(directory);
        return mapper.of(targetMember.getId(), usage);
    }

    private File getMemberDirectory(String memberId) {
        return new File(filePath + "/members/" + memberId);
    }
}
