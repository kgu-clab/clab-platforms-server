package page.clab.api.external.memberManagement.cloud.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.cloud.application.dto.response.CloudUsageInfo;
import page.clab.api.domain.memberManagement.cloud.application.port.in.RetrieveCloudUsageByMemberIdUseCase;
import page.clab.api.external.memberManagement.cloud.application.port.ExternalRetrieveCloudUsageByMemberIdUseCase;

@Service
@RequiredArgsConstructor
public class ExternalCloudUsageRetrievalByMemberIdService implements ExternalRetrieveCloudUsageByMemberIdUseCase {

    private final RetrieveCloudUsageByMemberIdUseCase retrieveCloudUsageByMemberIdUseCase;

    @Transactional(readOnly = true)
    @Override
    public CloudUsageInfo retrieveCloudUsage(String memberId) {
        return retrieveCloudUsageByMemberIdUseCase.retrieveCloudUsage(memberId);
    }
}
