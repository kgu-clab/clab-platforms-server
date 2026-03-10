package page.clab.api.domain.members.support.application.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.support.application.dto.mapper.SupportDtoMapper;
import page.clab.api.domain.members.support.application.dto.request.SupportRequestDto;
import page.clab.api.domain.members.support.application.port.in.RegisterSupportUseCase;
import page.clab.api.domain.members.support.application.port.out.RegisterSupportPort;
import page.clab.api.domain.members.support.domain.Support;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportRegisterService implements RegisterSupportUseCase {

    private final RegisterSupportPort registerSupportPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final UploadedFileService uploadedFileService;
    private final SupportDtoMapper mapper;

    @Transactional
    @Override
    public Long registerSupport(SupportRequestDto requestDto) {
        List<UploadedFile> uploadedFiles = uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrlList());
        MemberBasicInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberBasicInfo();
        Support support = mapper.fromDto(requestDto, currentMemberInfo, uploadedFiles);
        return registerSupportPort.save(support).getId();
    }
}
