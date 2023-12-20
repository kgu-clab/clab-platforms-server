package page.clab.api.service;

import java.awt.image.RescaleOp;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.exception.FileUploadFailException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.handler.FileHandler;
import page.clab.api.repository.FileRepository;
import page.clab.api.type.entity.File;
import page.clab.api.type.entity.Member;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private final FileHandler fileHandler;

    private final MemberService memberService;

    @Value("${resource.file.url}")
    private String fileURL; // 네트워크 상에서 파일 위치 식별

    public List<String> saveFiles(MultipartFile[] multipartFiles, String path) throws FileUploadFailException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String url = saveFile(multipartFile, path);
            urls.add(url);
        }
        return urls;
    }

    public String saveFile(MultipartFile multipartFile, String path) throws FileUploadFailException {
        Member member = memberService.getCurrentMember();
        if (path.startsWith("members/")) {
            String memberId = path.split("/")[1]; // members/{memberId}
            double usage = memberService.getCloudUsageByMemberId(memberId).getUsage();
            if (multipartFile.getSize() + usage > (10 * 1024 * 1024)) { // 10MB 제한
                return "저장 공간이 부족합니다.";
            }
        }

        String savedURL = fileHandler.saveFile(multipartFile, path);
        // File의 경우 RequestDto를 사용하지 않고 multipartFile을 파라미터로 받음.
        File file = new File();
        file.setOriginalFileName(multipartFile.getOriginalFilename());
        file.setSavedPath(savedURL);
        file.setFileSize(multipartFile.getSize());
        file.setContentType(multipartFile.getContentType());
        file.setUploader(member);

        int saveFileNameIndex = savedURL.lastIndexOf("/");
        file.setSaveFileName(savedURL.substring(saveFileNameIndex + 1));

        int categoryIndex = path.indexOf("/");
        String category = categoryIndex != -1 ? path.substring(0, categoryIndex) : path;
        file.setCategory(category);

        fileRepository.save(file);

        return fileURL + "/" + savedURL; //(카테고리/saveFileName)
    }

    public Long deleteFile(Long fileId) throws PermissionDeniedException{
        Member member = memberService.getCurrentMember();
        File file = getFileByIdOrThrow(fileId);
        if(!(file.getUploader().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))){
            throw new PermissionDeniedException("해당 파일을 삭제할 권한이 없습니다.");
        }

        //"/resources/files/activity-photos/1/216883240048200_8ad8eb2e-42c2-47cb-b31f-2b4204472c63.png"

        String filePath = fileURL + "/" + file.getSavedPath();
        java.io.File storedFile = new java.io.File(filePath);
        if(storedFile.exists()){
            if(storedFile.delete()){
                System.out.println("삭제 성공" + filePath);
            }
        }

        fileRepository.deleteById(fileId);

        return fileId;
    }

    public Resource downloadFile(Long fileId){
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File not found with id: " + fileId));
        Path filePath = Paths.get(fileURL + "/" + file.getSavedPath());
        try {
            // 파일 리소스 생성
            Resource resource = new UrlResource(filePath.toUri());

            // 파일이 존재하는지 확인
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new NotFoundException("File not found: ");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private File getFileByIdOrThrow(Long fileId){
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("해당 파일이 존재하지 않습니다."));
    }

}

