package page.clab.api.domain.member.application;

import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.global.exception.PermissionDeniedException;

public interface FetchCloudUsageByMemberIdService {
    CloudUsageInfo execute(String memberId) throws PermissionDeniedException;
}
