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

    private final MemberService memberService;

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
        if (path.startsWith("members/")) {
            String memberId = path.split("/")[1]; // members/{memberId}
            double usage = memberService.getCloudUsageByMemberId(memberId).getUsage();
            if (multipartFile.getSize() + usage > (10 * 1024 * 1024)) { // 10MB 제한
                return "저장 공간이 부족합니다.";
            }
        }
        String realFilename = fileHandler.saveFile(multipartFile, path);
        return fileURL + "/" + realFilename;
    }

}

