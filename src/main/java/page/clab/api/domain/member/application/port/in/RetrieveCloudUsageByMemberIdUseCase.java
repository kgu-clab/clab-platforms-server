package page.clab.api.domain.member.application.port.in;

import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.global.exception.PermissionDeniedException;

public interface RetrieveCloudUsageByMemberIdUseCase {
    CloudUsageInfo retrieveCloudUsage(String memberId) throws PermissionDeniedException;
}
