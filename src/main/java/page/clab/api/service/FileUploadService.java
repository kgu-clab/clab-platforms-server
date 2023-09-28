package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.exception.FileUploadFailException;
import page.clab.api.handler.FileHandler;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FileHandler fileHandler;

    @Value("${resource.file.url}")
    private String fileURL;

    public List<String> saveFiles(MultipartFile[] multipartFiles, String path) throws FileUploadFailException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String url = saveFile(multipartFile, path);
            urls.add(url);
        }
        return urls;
    }

    public String saveFile(MultipartFile multipartFile, String path) throws FileUploadFailException {
        String realFilename = fileHandler.saveFile(multipartFile, path);
        return fileURL + "/" + realFilename;
    }

}

