package page.clab.api.domain.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveCloudUsageByMemberIdUseCase;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.FileSystemUtil;

import java.io.File;

@Service
@RequiredArgsConstructor
public class CloudUsageRetrievalByMemberIdService implements RetrieveCloudUsageByMemberIdUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveMemberPort retrieveMemberPort;

    @Value("${resource.file.path}")
    private String filePath;

    @Override
    @Transactional(readOnly = true)
    public CloudUsageInfo retrieve(String memberId) throws PermissionDeniedException {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        Member targetMember = retrieveMemberPort.findByIdOrThrow(memberId);
        targetMember.validateAccessPermissionForCloud(currentMember);
        File directory = getMemberDirectory(targetMember.getId());
        long usage = FileSystemUtil.calculateDirectorySize(directory);
        return CloudUsageInfo.create(targetMember.getId(), usage);
    }

    private File getMemberDirectory(String memberId) {
        return new File(filePath + "/members/" + memberId);
    }
}
