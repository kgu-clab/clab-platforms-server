package page.clab.api.external.memberManagement.cloud.application.port;

import page.clab.api.domain.memberManagement.cloud.application.dto.response.CloudUsageInfo;

public interface ExternalRetrieveCloudUsageByMemberIdUseCase {

    CloudUsageInfo retrieveCloudUsage(String memberId);
}
