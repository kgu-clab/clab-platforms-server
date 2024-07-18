package page.clab.api.external.memberManagement.cloud.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.memberManagement.cloud.application.dto.response.CloudUsageInfo;
import page.clab.api.domain.memberManagement.cloud.application.port.in.RetrieveCloudUsageByMemberIdUseCase;
import page.clab.api.external.memberManagement.cloud.application.port.ExternalRetrieveCloudUsageByMemberIdUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ExternalCloudUsageRetrievalByMemberIdService implements ExternalRetrieveCloudUsageByMemberIdUseCase {

    private final RetrieveCloudUsageByMemberIdUseCase retrieveCloudUsageByMemberIdUseCase;


    @Override
    public CloudUsageInfo retrieveCloudUsage(String memberId) throws PermissionDeniedException {
        return retrieveCloudUsageByMemberIdUseCase.retrieveCloudUsage(memberId);
    }
}
