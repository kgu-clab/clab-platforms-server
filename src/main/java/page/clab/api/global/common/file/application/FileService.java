package page.clab.api.global.common.file.application;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.file.dao.UploadFileRepository;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.request.DeleteFileRequestDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.FileSystemUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final UploadFileRepository uploadFileRepository;

    private final FileHandler fileHandler;

    private final MemberService memberService;

    @Value("${resource.file.url}")
    private String fileURL;

    @Value("${resource.file.path}")
    private String filePath;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    public String saveQRCodeImage(byte[] QRCodeImage, String path, long storagePeriod, String nowDateTime) throws IOException {
        Member member = memberService.getCurrentMember();
        UploadedFile uploadedFile = new UploadedFile();

        String extension = "png";
        String originalFileName = path.replace(File.separator.toString(), "-") + nowDateTime;
        String url = fileURL + "/" + path.replace(File.separator.toString(), "/")
                + fileHandler.saveQRCodeImage(QRCodeImage, path, originalFileName, extension, uploadedFile);

        uploadedFile.setOriginalFileName(originalFileName);
        uploadedFile.setStoragePeriod(storagePeriod);
        uploadedFile.setContentType("image/png");
        uploadedFile.setUploader(member);
        uploadedFile.setUrl(url);
        uploadFileRepository.save(uploadedFile);
        return url;
    }

    public List<String> saveFiles(List<MultipartFile> multipartFiles, String path, long storagePeriod) throws IOException, PermissionDeniedException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String url = saveFile(multipartFile, path, storagePeriod);
            urls.add(url);
        }
        return urls;
    }

    public String saveFile(MultipartFile multipartFile, String path, long storagePeriod) throws IOException, PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (path.startsWith("members")) {
            String memberId = path.split(Pattern.quote(File.separator))[1];
            double usage = memberService.getCloudUsageByMemberId(memberId).getUsage();
            if (multipartFile.getSize() + usage > FileSystemUtil.convertToBytes(maxFileSize)) {
                return "저장 공간이 부족합니다.";
            }
        }
        UploadedFile uploadedFile = new UploadedFile();
        String url = fileURL + "/" + path.replace(File.separator.toString(), "/") + fileHandler.saveFile(multipartFile, path, uploadedFile);
        uploadedFile.setOriginalFileName(multipartFile.getOriginalFilename());
        uploadedFile.setStoragePeriod(storagePeriod);
        uploadedFile.setFileSize(multipartFile.getSize());
        uploadedFile.setContentType(multipartFile.getContentType());
        uploadedFile.setUploader(member);
        uploadedFile.setUrl(url);
        uploadFileRepository.save(uploadedFile);
        return url;
    }

    public String deleteFile(DeleteFileRequestDto deleteFileRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        String url = deleteFileRequestDto.getUrl();
        UploadedFile uploadedFile = uploadFileRepository.findByUrl(url);
        String filePath = uploadedFile.getSavedPath();
        File storedFile = new File(filePath);
        if (uploadedFile == null || !storedFile.exists()) {
            throw new NotFoundException("존재하지 않는 파일입니다.");
        }
        if (!(uploadedFile.getUploader().getId().equals(member.getId()) || memberService.isMemberSuperRole(member))) {
            throw new PermissionDeniedException("해당 파일을 삭제할 권한이 없습니다.");
        }
        if (!storedFile.delete()) {
            log.info("파일 삭제 오류 : {}", filePath);
        }
        String deletedFileUrl = uploadedFile.getUrl();
        uploadFileRepository.deleteById(uploadedFile.getId());
        return deletedFileUrl;
    }

}

