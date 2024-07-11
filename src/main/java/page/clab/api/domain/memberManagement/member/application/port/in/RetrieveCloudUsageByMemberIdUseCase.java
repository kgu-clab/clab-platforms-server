package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.domain.memberManagement.member.application.dto.response.CloudUsageInfo;
import page.clab.api.global.exception.PermissionDeniedException;

public interface RetrieveCloudUsageByMemberIdUseCase {
    CloudUsageInfo retrieveCloudUsage(String memberId) throws PermissionDeniedException;
}
