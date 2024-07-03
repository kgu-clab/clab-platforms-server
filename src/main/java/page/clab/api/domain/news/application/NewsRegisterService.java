package page.clab.api.domain.news.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.port.in.RegisterNewsUseCase;
import page.clab.api.domain.news.application.port.out.RegisterNewsPort;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.dto.request.NewsRequestDto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class NewsRegisterService implements RegisterNewsUseCase {

    private final UploadedFileService uploadedFileService;
    private final ValidationService validationService;
    private final RegisterNewsPort registerNewsPort;

    @Transactional
    @Override
    public Long register(NewsRequestDto requestDto) {
        News news = NewsRequestDto.toEntity(requestDto);
        validationService.checkValid(news);
        news.setUploadedFiles(uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrlList()));
        return registerNewsPort.save(news).getId();
    }
}
