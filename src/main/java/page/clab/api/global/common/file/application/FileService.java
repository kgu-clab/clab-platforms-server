package page.clab.api.global.common.file.application;


import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupAdminService;
import page.clab.api.domain.activity.activitygroup.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activity.activitygroup.dao.ActivityGroupRepository;
import page.clab.api.domain.activity.activitygroup.dao.GroupMemberRepository;
import page.clab.api.domain.activity.activitygroup.domain.GroupMemberStatus;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.external.memberManagement.cloud.application.port.ExternalRetrieveCloudUsageByMemberIdUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.auth.util.AuthUtil;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.request.DeleteFileRequestDto;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;
import page.clab.api.global.common.file.exception.AssignmentFileUploadFailException;
import page.clab.api.global.common.file.exception.CloudStorageNotEnoughException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.FileSystemUtil;
import page.clab.api.global.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileHandler fileHandler;
    private final UploadedFileService uploadedFileService;
    private final ActivityGroupAdminService activityGroupAdminService;
    private final ActivityGroupRepository activityGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ActivityGroupBoardRepository activityGroupBoardRepository;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalRetrieveCloudUsageByMemberIdUseCase externalRetrieveCloudUsageByMemberIdUseCase;

    @Value("${resource.file.url}")
    private String fileURL;

    @Value("${resource.file.path}")
    private String filePath;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    private static final Map<Role, Set<String>> roleCategoryMap = Map.of(
            Role.GUEST, Set.of("boards", "profiles", "activity-photos", "membership-fees"),
            Role.USER, Set.of("boards", "profiles", "activity-photos", "membership-fees" , "weekly-activities", "assignments"),
            Role.ADMIN, Set.of("boards", "profiles", "activity-photos", "membership-fees", "weekly-activities", "members", "assignments"),
            Role.SUPER, Set.of("boards", "profiles", "activity-photos", "membership-fees", "weekly-activities", "members", "assignments")
    );

    private final Map<String, BiFunction<String, Authentication, Boolean>> categoryAccessMap = Map.of(
            "boards", (url, auth) -> true,
            "profiles", (url, auth) -> true,
            "membership-fees", (url, auth) -> true,
            "activity-photos", (url, auth) -> true,
            "weekly-activities", this::isWeeklyActivityAccessible,
            "members", this::isMemberAccessible,
            "assignments", this::isAssignmentAccessible
    );

    public String saveQRCodeImage(byte[] QRCodeImage, String path, long storagePeriod, String nowDateTime) throws IOException {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        String extension = "png";
        String originalFileName = path.replace(File.separator, "-") + nowDateTime;
        String saveFilename = FileUtil.makeFileName(extension);
        String savePath = filePath + File.separator + path + File.separator + saveFilename;
        String url = fileURL + "/" + path.replace(File.separator, "/") + "/" + saveFilename;

        fileHandler.saveQRCodeImage(QRCodeImage, path, saveFilename, extension, filePath);
        UploadedFile uploadedFile = UploadedFile.create(currentMemberId, originalFileName, saveFilename, savePath, url, (long) QRCodeImage.length, "image/png", storagePeriod, path);
        uploadedFileService.saveUploadedFile(uploadedFile);
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
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();

        validatePathVariable(path);
        validateMemberCloudUsage(multipartFile, path);
        checkAndRemoveExistingFile(path);

        String savedFilePath = fileHandler.saveFile(multipartFile, path, filePath);
        String fileName = new File(savedFilePath).getName();
        String url = fileURL + "/" + path.replace(File.separator, "/") + "/" + fileName;

        UploadedFile uploadedFile = UploadedFile.create(currentMemberId, multipartFile.getOriginalFilename(), fileName, savedFilePath, url, multipartFile.getSize(), multipartFile.getContentType(), storagePeriod, path);
        uploadedFileService.saveUploadedFile(uploadedFile);
        return UploadedFileResponseDto.toDto(uploadedFile);
    }

    public String deleteFile(DeleteFileRequestDto deleteFileRequestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        UploadedFile uploadedFile = uploadedFileService.getUploadedFileByUrl(deleteFileRequestDto.getUrl());

        String filePath = uploadedFile.getSavedPath();
        File storedFile = new File(filePath);
        if (!storedFile.exists()) {
            throw new NotFoundException("존재하지 않는 파일입니다.");
        }

        uploadedFile.validateAccessPermission(currentMemberInfo);
        fileHandler.deleteFile(uploadedFile.getSavedPath());
        return uploadedFile.getUrl();
    }

    public String buildPath(String baseDirectory, Long... additionalSegments) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        StringBuilder pathBuilder = new StringBuilder(baseDirectory);
        for (Long segment : additionalSegments) {
            pathBuilder.append(File.separator).append(segment);
        }
        pathBuilder.append(File.separator).append(currentMemberId);
        return pathBuilder.toString();
    }

    public void validatePathVariable(String path) throws AssignmentFileUploadFailException {
        if (path.split(Pattern.quote(File.separator))[0].equals("assignment")) {
            Long activityGroupId = Long.parseLong(path.split(Pattern.quote(File.separator))[1]);
            Long activityGroupBoardId = Long.parseLong(path.split(Pattern.quote(File.separator))[2]);
            String memberId = path.split(Pattern.quote(File.separator))[3];
            Optional<Member> assignmentWriterOpt = externalRetrieveMemberUseCase.findById(memberId);

            if (!activityGroupRepository.existsById(activityGroupId)) {
                throw new AssignmentFileUploadFailException("해당 활동은 존재하지 않습니다.");
            }
            if (assignmentWriterOpt.isEmpty() || !groupMemberRepository.existsByMemberIdAndActivityGroupId(assignmentWriterOpt.get().getId(), activityGroupId)) {
                throw new AssignmentFileUploadFailException("해당 활동에 참여하고 있지 않은 멤버입니다.");
            }
            if (!activityGroupBoardRepository.existsById(activityGroupBoardId)) {
                throw new AssignmentFileUploadFailException("해당 활동 그룹 게시판이 존재하지 않습니다.");
            }
        }
    }

    private void validateMemberCloudUsage(MultipartFile multipartFile, String path) throws PermissionDeniedException {
        if (path.split(Pattern.quote(File.separator))[0].equals("members")) {
            String memberId = path.split(Pattern.quote(File.separator))[1];
            double usage = externalRetrieveCloudUsageByMemberIdUseCase.retrieveCloudUsage(memberId).getUsage();

            if (multipartFile.getSize() + usage > FileSystemUtil.convertToBytes(maxFileSize)) {
                throw new CloudStorageNotEnoughException("클라우드 저장 공간이 부족합니다.");
            }
        }
    }

    private void checkAndRemoveExistingFile(String path) {
        List<String> validPrefixes = Arrays.asList("profiles", "members/", "assignments");
        boolean shouldDelete = validPrefixes.stream().anyMatch(path::startsWith);
        if (shouldDelete) {
            UploadedFile fileToDelete = uploadedFileService.getUniqueUploadedFileByCategory(path);
            if (fileToDelete != null) {
                fileHandler.deleteFile(fileToDelete.getSavedPath());
            }
        }
    }

    public boolean isUserAccessibleAtFile(Authentication authentication, String url) {
        String category = getCategoryByUrl(url);
        if (category == null || category.isEmpty())
            return false;
        return isUserAccessibleByCategory(category, url, authentication);
    }

    public boolean isUserAccessibleByCategory(String category, String url, Authentication authentication) {
        if (category.equals("activity-photos")) {
            return true;
        }
        if (AuthUtil.isUserUnAuthenticated(authentication)) {
            return false;
        }

        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String roleName = authority.getAuthority().replace("ROLE_", "");
        Role role = Role.valueOf(roleName);

        if (!roleCategoryMap.getOrDefault(role, Set.of()).contains(category)) {
            return false;
        }

        return categoryAccessMap.getOrDefault(category, (u, a) -> false).apply(url, authentication);
    }

    private boolean isMemberAccessible(String url, Authentication authentication) {
        UploadedFile uploadedFile = uploadedFileService.getUploadedFileByUrl(url);
        String uploaderId = uploadedFile.getUploader();
        return authentication.getName().equals(uploaderId);
    }

    private boolean isAssignmentAccessible(String url, Authentication authentication) {
        UploadedFile uploadedFile = uploadedFileService.getUploadedFileByUrl(url);
        String uploaderId = uploadedFile.getUploader();
        String[] parts = url.split("/");
        Long activityGroupId = Long.parseLong(parts[4]);

        return authentication.getName().equals(uploaderId) ||
                activityGroupAdminService.isMemberGroupLeaderRole(activityGroupId, authentication.getName());
    }

    private boolean isWeeklyActivityAccessible(String url, Authentication authentication) {
        String memberId = authentication.getName();
        String[] parts = url.split("/");
        Long activityGroupId = Long.parseLong(parts[4]);

        return groupMemberRepository.existsByActivityGroupIdAndMemberIdAndStatus(activityGroupId, memberId, GroupMemberStatus.ACCEPTED);
    }

    private String getCategoryByUrl(String url) {
        String basePath = fileURL + "/";
        String category = "";
        if (url.startsWith(basePath)) {
            category = url.substring(basePath.length());
            if (category.contains("/")) {
                category = category.substring(0, category.indexOf('/'));
            }
        }
        return category;
    }
}

