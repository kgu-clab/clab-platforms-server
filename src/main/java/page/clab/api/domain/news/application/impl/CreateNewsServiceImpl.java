package page.clab.api.domain.news.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.CreateNewsService;
import page.clab.api.domain.news.dao.NewsRepository;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.dto.request.NewsRequestDto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CreateNewsServiceImpl implements CreateNewsService {

    private final UploadedFileService uploadedFileService;
    private final ValidationService validationService;
    private final NewsRepository newsRepository;

    @Transactional
    @Override
    public Long execute(NewsRequestDto requestDto) {
        News news = NewsRequestDto.toEntity(requestDto);
        validationService.checkValid(news);
        news.setUploadedFiles(uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrlList()));
        return newsRepository.save(news).getId();
    }
}
