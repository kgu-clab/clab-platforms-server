package page.clab.api.domain.member.application;

import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.global.exception.PermissionDeniedException;

public interface CloudUsageRetrievalByMemberIdUseCase {
    CloudUsageInfo retrieve(String memberId) throws PermissionDeniedException;
}
