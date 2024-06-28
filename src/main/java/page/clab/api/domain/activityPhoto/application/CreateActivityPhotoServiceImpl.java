package page.clab.api.domain.activityPhoto.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityPhoto.dao.ActivityPhotoRepository;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.domain.activityPhoto.dto.request.ActivityPhotoRequestDto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateActivityPhotoServiceImpl implements CreateActivityPhotoService {

    private final ActivityPhotoRepository activityPhotoRepository;
    private final UploadedFileService uploadedFileService;

    @Transactional
    @Override
    public Long execute(ActivityPhotoRequestDto requestDto) {
        List<UploadedFile> uploadedFiles = uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrlList());
        ActivityPhoto activityPhoto = ActivityPhotoRequestDto.toEntity(requestDto, uploadedFiles);
        return activityPhotoRepository.save(activityPhoto).getId();
    }
}