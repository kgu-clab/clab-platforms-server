package page.clab.api.global.common.file.application;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.domain.activityGroup.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activityGroup.dao.ActivityGroupRepository;
import page.clab.api.domain.activityGroup.dao.GroupMemberRepository;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.file.dao.UploadFileRepository;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.request.DeleteFileRequestDto;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;
import page.clab.api.global.common.file.exception.CloudStorageNotEnoughException;
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

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final ActivityGroupBoardRepository activityGroupBoardRepository;

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

    public List<UploadedFileResponseDto> saveFiles(List<MultipartFile> multipartFiles, String path, long storagePeriod) throws IOException, PermissionDeniedException {
        List<UploadedFileResponseDto> uploadedFileResponseDtos = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            UploadedFileResponseDto responseDto = saveFile(multipartFile, path, storagePeriod);
            uploadedFileResponseDtos.add(responseDto);
        }
        return uploadedFileResponseDtos;
    }

    public UploadedFileResponseDto saveFile(MultipartFile multipartFile, String path, long storagePeriod) throws IOException, PermissionDeniedException {
        Member member = memberService.getCurrentMember();

        UploadedFile existingUploadedFile = getUploadedFileByCategoryAndOriginalName(path, multipartFile.getOriginalFilename());
        if (existingUploadedFile != null) {
            uploadFileRepository.delete(existingUploadedFile);
        }

        if (!path.startsWith("membership-fee") && path.startsWith("members")) {
            String memberId = path.split(Pattern.quote(File.separator))[1];
            double usage = memberService.getCloudUsageByMemberId(memberId).getUsage();
            if (multipartFile.getSize() + usage > FileSystemUtil.convertToBytes(maxFileSize)) {
                throw new CloudStorageNotEnoughException("클라우드 저장 공간이 부족합니다.");
            }
        }
        if (path.startsWith("assignment")) {
            Long activityGroupId = Long.parseLong(path.split(Pattern.quote(File.separator))[1]);
            Long activityGroupBoardId = Long.parseLong(path.split(Pattern.quote(File.separator))[2]);
            String memberId = path.split(Pattern.quote(File.separator))[3];
            Member assignmentWriter = memberService.getMemberById(memberId);
            if (!activityGroupRepository.existsById(activityGroupId)) {
                throw new NotFoundException("해당 활동은 존재하지 않습니다.");
            }
            if (!groupMemberRepository.existsByMemberAndActivityGroupId(assignmentWriter, activityGroupId)) {
                throw new NotFoundException("해당 활동에 참여하고 있지 않은 멤버입니다.");
            }
            if (!activityGroupBoardRepository.existsById(activityGroupBoardId)) {
                throw new NotFoundException("해당 활동그룹 게시판이 존재하지 않습니다.");
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

        return UploadedFileResponseDto.builder()
                .fileUrl(url)
                .originalFileName(uploadedFile.getOriginalFileName())
                .storageDateTimeOfFile(getStorageDateTimeOfFile(url))
                .build();
    }

    public String deleteFile(DeleteFileRequestDto deleteFileRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        String url = deleteFileRequestDto.getUrl();
        UploadedFile uploadedFile = getUploadedFileByUrl(url);
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

    public LocalDateTime getStorageDateTimeOfFile(String fileUrl) {
        UploadedFile uploadedFile = getUploadedFileByUrl(fileUrl);
        if (uploadedFile == null) {
            throw new NotFoundException("파일이 존재하지 않습니다.");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdDateTime = uploadedFile.getCreatedAt();
        Long storagePeriod =  uploadedFile.getStoragePeriod();

        return createdDateTime.plusDays(storagePeriod);
    }

    public UploadedFile getUploadedFileByUrl(String url) {
        return uploadFileRepository.findByUrl(url)
                .orElseThrow(() -> new NotFoundException("파일을 찾을 수 없습니다."));
    }

    public UploadedFile getUploadedFileByCategoryAndOriginalName(String category, String originalName) {
        return uploadFileRepository.findByCategoryAndOriginalFileName(category, originalName);
    }

    public String getOriginalFileNameByUrl(String url) {
        return getUploadedFileByUrl(url).getOriginalFileName();
    }

}

