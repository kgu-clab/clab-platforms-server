package page.clab.api.global.common.file.application;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.domain.activityGroup.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activityGroup.dao.ActivityGroupRepository;
import page.clab.api.domain.activityGroup.dao.GroupMemberRepository;
import page.clab.api.domain.member.application.MemberCloudService;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.request.DeleteFileRequestDto;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;
import page.clab.api.global.common.file.exception.AssignmentFileUploadFailException;
import page.clab.api.global.common.file.exception.CloudStorageNotEnoughException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.FileSystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileHandler fileHandler;

    private final MemberService memberService;

    private final MemberCloudService memberCloudService;

    private final UploadedFileService uploadedFileService;

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
        Member currentMember = memberService.getCurrentMember();
        String extension = "png";
        String originalFileName = path.replace(File.separator, "-") + nowDateTime;
        String saveFilename = fileHandler.makeSaveFileName(extension);
        String savePath = filePath + File.separator + path + File.separator + saveFilename;
        String url = fileURL + "/" + path.replace(File.separator, "/") + "/" + saveFilename;

        fileHandler.saveQRCodeImage(QRCodeImage, path, saveFilename, extension);
        UploadedFile uploadedFile = UploadedFile.create(currentMember, originalFileName, saveFilename, savePath, url, (long) QRCodeImage.length, "image/png", storagePeriod, path);
        uploadedFileService.saveUploadedFile(uploadedFile);
        return url;
    }

    public String buildPath(String baseDirectory, Long... additionalSegments) {
        Member currentMember = memberService.getCurrentMember();
        StringBuilder pathBuilder = new StringBuilder(baseDirectory);
        for (Long segment : additionalSegments) {
            pathBuilder.append(File.separator).append(segment);
        }
        pathBuilder.append(File.separator).append(currentMember.getId());
        return pathBuilder.toString();
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
        Member currentMember = memberService.getCurrentMember();

        if (!isValidPathVariable(path)) {
            throw new NotFoundException("파일 업로드 api 요청 pathVariable이 유효하지 않습니다.");
        }

        if (!path.startsWith("membership-fee") && path.startsWith("members")) {
            String memberId = path.split(Pattern.quote(File.separator))[1];
            double usage = memberCloudService.getCloudUsageByMemberId(memberId).getUsage();
            if (multipartFile.getSize() + usage > FileSystemUtil.convertToBytes(maxFileSize)) {
                throw new CloudStorageNotEnoughException("클라우드 저장 공간이 부족합니다.");
            }
        }

        UploadedFile existingUploadedFile = uploadedFileService.getUniqueUploadedFileByCategoryAndOriginalName(path, multipartFile.getOriginalFilename());
        if (existingUploadedFile != null) {
            deleteFileBySavedPath(existingUploadedFile.getSavedPath());
        }

        if (path.startsWith("profiles")) {
            UploadedFile profileFile = uploadedFileService.getUniqueUploadedFileByCategory(path);
            if (profileFile != null) {
                deleteFileBySavedPath(profileFile.getSavedPath());
            }
        }

        UploadedFile uploadedFile = new UploadedFile();
        String url = fileURL + "/" + path.replace(File.separator.toString(), "/") + fileHandler.saveFile(multipartFile, path, uploadedFile);
        uploadedFile.setOriginalFileName(multipartFile.getOriginalFilename());
        uploadedFile.setStoragePeriod(storagePeriod);
        uploadedFile.setFileSize(multipartFile.getSize());
        uploadedFile.setContentType(multipartFile.getContentType());
        uploadedFile.setUploader(currentMember);
        uploadedFile.setUrl(url);
        uploadedFileService.saveUploadedFile(uploadedFile);

        return UploadedFileResponseDto.toDto(uploadedFile);
    }

    public boolean isValidPathVariable(String path) throws AssignmentFileUploadFailException {
        switch (path.split(Pattern.quote(File.separator))[0]) {
            case "assignment" : {
                Long activityGroupId = Long.parseLong(path.split(Pattern.quote(File.separator))[1]);
                Long activityGroupBoardId = Long.parseLong(path.split(Pattern.quote(File.separator))[2]);
                String memberId = path.split(Pattern.quote(File.separator))[3];
                Member assignmentWriter = memberService.getMemberById(memberId);
                if (!activityGroupRepository.existsById(activityGroupId)) {
                    throw new AssignmentFileUploadFailException("해당 활동은 존재하지 않습니다.");
                }
                if (!groupMemberRepository.existsByMemberAndActivityGroupId(assignmentWriter, activityGroupId)) {
                    throw new AssignmentFileUploadFailException("해당 활동에 참여하고 있지 않은 멤버입니다.");
                }
                if (!activityGroupBoardRepository.existsById(activityGroupBoardId)) {
                    throw new AssignmentFileUploadFailException("해당 활동 그룹 게시판이 존재하지 않습니다.");
                }
                return true;
            }
        }
        return true;
    }

    public String deleteFile(DeleteFileRequestDto deleteFileRequestDto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        String url = deleteFileRequestDto.getUrl();
        UploadedFile uploadedFile = uploadedFileService.getUploadedFileByUrl(url);
        String filePath = uploadedFile.getSavedPath();
        File storedFile = new File(filePath);
        if (uploadedFile == null || !storedFile.exists()) {
            throw new NotFoundException("존재하지 않는 파일입니다.");
        }
        uploadedFile.validateAccessPermission(currentMember);
        if (!storedFile.delete()) {
            log.info("파일 삭제 오류 : {}", filePath);
        }
        String deletedFileUrl = uploadedFile.getUrl();
        deleteFileBySavedPath(filePath);
        return deletedFileUrl;
    }

    public void deleteFileBySavedPath(String savedPath) {
        File existingFile = new File(savedPath);
        if (existingFile != null) {
            existingFile.delete();
        }
    }

}

