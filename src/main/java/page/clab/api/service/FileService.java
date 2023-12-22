package page.clab.api.service;


import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import page.clab.api.type.entity.FileEntity;
import page.clab.api.type.entity.Member;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private final FileHandler fileHandler;

    private final MemberService memberService;

    @Value("${resource.file.url}")
    private String fileURL; // 네트워크 상에서 파일 위치 식별

    @Value("${resource.file.path}")
    private String filePath; //실제 저장 위치

    public List<String> saveFiles(List<MultipartFile> multipartFiles, String path, long storagePeriod) throws FileUploadFailException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String url = saveFile(multipartFile, path, storagePeriod);
            urls.add(url);
        }
        return urls;
    }

    public String saveFile(MultipartFile multipartFile, String path, long storagePeriod) throws FileUploadFailException {

        Logger logger = LoggerFactory.getLogger(this.getClass());

        Member member = memberService.getCurrentMember();

        if (path.startsWith("members")) {
            String memberId = path.split(File.separator)[1]; // members\{memberId}
            double usage = memberService.getCloudUsageByMemberId(memberId).getUsage();
            if (multipartFile.getSize() + usage > (10 * 1024 * 1024)) { // 10MB 제한
                return "저장 공간이 부족합니다.";
            }
        }

        FileEntity fileEntity = new FileEntity();

        String url = fileURL + "/" + path.replace(File.separator.toString(), "/") + fileHandler.saveFile(multipartFile, path, fileEntity);
        // FileEntity의 경우 RequestDto를 사용하지 않고 multipartFile을 파라미터로 받음.

        fileEntity.setOriginalFileName(multipartFile.getOriginalFilename());
        fileEntity.setStoragePeriod(storagePeriod);
        fileEntity.setFileSize(multipartFile.getSize());
        fileEntity.setContentType(multipartFile.getContentType());
        fileEntity.setUploader(member);
        fileEntity.setUrl(url);

        fileRepository.save(fileEntity);

        logger.info(fileEntity.getSavedPath());

        return url;
    }

    public String deleteFile(String saveFileName) throws PermissionDeniedException{

        Logger logger = LoggerFactory.getLogger(this.getClass());

        Member member = memberService.getCurrentMember();

        FileEntity fileEntity = fileRepository.findBySaveFileName(saveFileName);

        if(fileEntity == null){
            throw new NotFoundException("파일 이름에 해당되는 DB 정보가 없습니다.");
        }

        if(!(fileEntity.getUploader().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))){
            throw new PermissionDeniedException("해당 파일을 삭제할 권한이 없습니다.");
        }

        String filePath = fileEntity.getSavedPath();
        File storedFile = new File(filePath);

        if(!storedFile.exists()){
            throw new NotFoundException("파일이 존재하지 않습니다.");
        }

        if(!storedFile.delete()){
            logger.info("파일 삭제 실패"); // 삭제 실패 exception 만들기..?
        }

        String url = fileEntity.getUrl();
        fileRepository.deleteById(fileEntity.getId()); //deleteBySaveFileName을 만들어서 사용하는거랑 어떤게 더 효율?

        return url;
    }

/*    public Resource downloadFile(Long fileId){
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("FileEntity not found with id: " + fileId));
        Path filePath = Paths.get(fileURL + "/" + fileEntity.getSavedPath());
        try {
            // 파일 리소스 생성
            Resource resource = new UrlResource(filePath.toUri());

            // 파일이 존재하는지 확인
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new NotFoundException("FileEntity not found: ");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }*/

    /*private FileEntity getFileByIdOrThrow(Long fileId){
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("해당 파일이 존재하지 않습니다."));
    }
*/
}

