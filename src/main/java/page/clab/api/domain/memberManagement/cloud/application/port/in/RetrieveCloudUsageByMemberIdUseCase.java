package page.clab.api.domain.memberManagement.cloud.application.port.in;

import page.clab.api.domain.memberManagement.cloud.application.dto.response.CloudUsageInfo;

public interface RetrieveCloudUsageByMemberIdUseCase {

    CloudUsageInfo retrieveCloudUsage(String memberId);
}
