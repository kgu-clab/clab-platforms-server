package page.clab.api.domain.activityPhoto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityPhoto.application.dto.request.ActivityPhotoRequestDto;
import page.clab.api.domain.activityPhoto.application.port.in.RegisterActivityPhotoUseCase;
import page.clab.api.domain.activityPhoto.application.port.out.RegisterActivityPhotoPort;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityPhotoRegisterService implements RegisterActivityPhotoUseCase {

    private final RegisterActivityPhotoPort registerActivityPhotoPort;
    private final UploadedFileService uploadedFileService;

    @Transactional
    @Override
    public Long registerActivityPhoto(ActivityPhotoRequestDto requestDto) {
        List<UploadedFile> uploadedFiles = uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrlList());
        ActivityPhoto activityPhoto = ActivityPhotoRequestDto.toEntity(requestDto, uploadedFiles);
        return registerActivityPhotoPort.save(activityPhoto).getId();
    }
}
