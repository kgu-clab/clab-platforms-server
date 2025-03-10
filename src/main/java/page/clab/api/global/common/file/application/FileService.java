package page.clab.api.global.common.file.application;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupAdminService;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupBoardService;
import page.clab.api.domain.activity.activitygroup.dao.ActivityGroupRepository;
import page.clab.api.domain.activity.activitygroup.dao.GroupMemberRepository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.GroupMemberStatus;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.external.memberManagement.cloud.application.port.ExternalRetrieveCloudUsageByMemberIdUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.auth.util.AuthUtil;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.mapper.FileDtoMapper;
import page.clab.api.global.common.file.dto.request.DeleteFileRequestDto;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;
import page.clab.api.global.util.FileSystemUtil;
import page.clab.api.global.util.FileUtil;

/**
 * {@code FileService}는 파일 저장, 삭제, 경로 검증 및 파일 접근 권한 관리와 같은 파일 관리 기능을 제공하는 클래스입니다.
 *
 * <p>이 서비스는 다양한 카테고리에 대해 파일 업로드 및 QR 코드 이미지 저장, 파일 삭제, 멤버의 클라우드 사용량 검증 등의 기능을 수행합니다.
 * 또한 파일 접근 권한을 역할(Role) 및 경로에 따라 관리하여 안전한 파일 접근을 보장합니다.</p>
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>{@link #saveQRCodeImage(byte[], String, long, String)} - QR 코드 이미지를 파일로 저장합니다.</li>
 *     <li>{@link #saveFiles(List, String, long)} - 여러 개의 파일을 저장하고 파일 접근 권한을 검증합니다.</li>
 *     <li>{@link #saveFile(MultipartFile, String, long)} - 단일 파일을 저장하고 접근 권한을 검증합니다.</li>
 *     <li>{@link #deleteFile(DeleteFileRequestDto)} - 파일을 삭제하고 파일의 존재 여부와 접근 권한을 확인합니다.</li>
 *     <li>{@link #validatePathVariable(String)} - 경로 유효성 및 접근 권한을 검증하여 파일 저장 경로의 적합성을 확인합니다.</li>
 * </ul>
 *
 * <p>각 역할(Role)에 따른 파일 접근 제한 및 유효성 검증을 통해 파일 시스템의 보안을 유지하며, 유저별 클라우드 저장 용량을 제한하는 검증 로직을 포함하고 있습니다.</p>
 */
@Service
@RequiredArgsConstructor
public class FileService {

    private static final Map<Role, Set<String>> roleCategoryMap = Map.of(
        Role.GUEST, Set.of("boards", "profiles", "activity-photos", "membership-fees", "executives"),
        Role.USER,
        Set.of("boards", "profiles", "activity-photos", "membership-fees", "notices", "weekly-activities", "members",
            "assignments", "submits", "executives"),
        Role.ADMIN,
        Set.of("boards", "profiles", "activity-photos", "membership-fees", "notices", "weekly-activities", "members",
            "assignments", "submits", "executives"),
        Role.SUPER,
        Set.of("boards", "profiles", "activity-photos", "membership-fees", "notices", "weekly-activities", "members",
            "assignments", "submits", "executives")
    );
    private final FileHandler fileHandler;
    private final UploadedFileService uploadedFileService;
    private final ActivityGroupAdminService activityGroupAdminService;
    private final ActivityGroupBoardService activityGroupBoardService;
    private final ActivityGroupRepository activityGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalRetrieveCloudUsageByMemberIdUseCase externalRetrieveCloudUsageByMemberIdUseCase;
    private final FileDtoMapper mapper;
    private final Map<String, BiFunction<String, Authentication, Boolean>> categoryAccessMap = Map.of(
        "boards", (url, auth) -> true,
        "profiles", (url, auth) -> true,
        "executives", (url, auth) -> true,
        "activity-photos", (url, auth) -> true,
        "membership-fees", (url, auth) -> true,
        "notices", this::isNonSubmitCategoryAccessible,
        "weekly-activities", this::isNonSubmitCategoryAccessible,
        "assignments", this::isNonSubmitCategoryAccessible,
        "members", this::isMemberAccessible,
        "submits", this::isSubmitAccessible
    );
    @Value("${resource.file.url}")
    private String fileURL;
    @Value("${resource.file.path}")
    private String filePath;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    public String saveQRCodeImage(byte[] QRCodeImage, String path, long storagePeriod, String nowDateTime)
        throws IOException {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        String extension = "png";
        String originalFileName = path.replace(File.separator, "-") + nowDateTime;
        String saveFilename = FileUtil.makeFileName(extension);
        String savePath = filePath + File.separator + path + File.separator + saveFilename;
        String url = fileURL + "/" + path.replace(File.separator, "/") + "/" + saveFilename;

        fileHandler.saveQRCodeImage(QRCodeImage, path, saveFilename, extension, filePath);
        UploadedFile uploadedFile = UploadedFile.create(currentMemberId, originalFileName, saveFilename, savePath, url,
            (long) QRCodeImage.length, "image/png", storagePeriod, path);
        uploadedFileService.saveUploadedFile(uploadedFile);
        return url;
    }

    public List<UploadedFileResponseDto> saveFiles(List<MultipartFile> multipartFiles, String path, long storagePeriod)
        throws IOException {
        List<UploadedFileResponseDto> uploadedFileResponseDtos = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            UploadedFileResponseDto responseDto = saveFile(multipartFile, path, storagePeriod);
            uploadedFileResponseDtos.add(responseDto);
        }
        return uploadedFileResponseDtos;
    }

    public UploadedFileResponseDto saveFile(MultipartFile multipartFile, String path, long storagePeriod)
        throws IOException {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();

        validatePathVariable(path);
        validateMemberCloudUsage(multipartFile, path);
        checkAndRemoveExistingFile(path);

        String savedFilePath = fileHandler.saveFile(multipartFile, path, filePath);
        String fileName = new File(savedFilePath).getName();
        String url = fileURL + "/" + path.replace(File.separator, "/") + "/" + fileName;

        UploadedFile uploadedFile = UploadedFile.create(currentMemberId, multipartFile.getOriginalFilename(), fileName,
            savedFilePath, url, multipartFile.getSize(), multipartFile.getContentType(), storagePeriod, path);
        uploadedFileService.saveUploadedFile(uploadedFile);
        return mapper.toDto(uploadedFile);
    }

    public String deleteFile(DeleteFileRequestDto deleteFileRequestDto) {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        UploadedFile uploadedFile = uploadedFileService.getUploadedFileByUrl(deleteFileRequestDto.getUrl());

        String filePath = uploadedFile.getSavedPath();
        File storedFile = new File(filePath);
        if (!storedFile.exists()) {
            throw new BaseException(ErrorCode.NOT_FOUND, "존재하지 않는 파일입니다.");
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
        if (checkExecutivesDirectory(baseDirectory)) {
            return pathBuilder.toString();
        }
        pathBuilder.append(File.separator).append(currentMemberId);
        return pathBuilder.toString();
    }

    public void validatePathVariable(String path) {
        String[] pathParts = path.split(Pattern.quote(File.separator));
        String pathStart = pathParts[0];

        switch (pathStart) {
            case "notices":
                validateNoticePath(pathParts);
                break;
            case "weekly-activities":
                validateWeeklyActivityPath(pathParts);
                break;
            case "assignments":
                validateAssignmentPath(pathParts);
                break;
            case "submits":
                validateSubmitPath(pathParts);
                break;
        }
    }

    private boolean checkExecutivesDirectory(String baseDirectory) {
        return baseDirectory.equals("executives");
    }

    private void validateNoticePath(String[] pathParts) {
        Long activityGroupId = parseId(pathParts[1], "활동 ID가 유효하지 않습니다.");
        String memberId = externalRetrieveMemberUseCase.getCurrentMemberId();

        validateActivityGroupExist(activityGroupId);
        validateIsMemberPartOfActivity(memberId, activityGroupId);
        validateIsMemberGroupLeader(activityGroupId, memberId, "활동의 공지 관련 파일은 리더만 등록할 수 있습니다.");
    }

    private void validateWeeklyActivityPath(String[] pathParts) {
        Long activityGroupId = parseId(pathParts[1], "활동 ID가 유효하지 않습니다.");
        String memberId = externalRetrieveMemberUseCase.getCurrentMemberId();

        validateActivityGroupExist(activityGroupId);
        validateIsMemberPartOfActivity(memberId, activityGroupId);
        validateIsMemberGroupLeader(activityGroupId, memberId, "활동의 주차별 활동 관련 파일은 리더만 등록할 수 있습니다.");
    }

    private void validateAssignmentPath(String[] pathParts) {
        Long activityGroupId = parseId(pathParts[1], "활동 ID가 유효하지 않습니다.");
        String memberId = externalRetrieveMemberUseCase.getCurrentMemberId();

        validateActivityGroupExist(activityGroupId);
        validateIsMemberPartOfActivity(memberId, activityGroupId);
        validateIsMemberGroupLeader(activityGroupId, memberId, "활동의 과제 관련 파일은 리더만 등록할 수 있습니다.");
    }

    private void validateSubmitPath(String[] pathParts) {
        Long activityGroupId = parseId(pathParts[1], "활동 ID가 유효하지 않습니다.");
        Long activityGroupBoardId = parseId(pathParts[2], "활동 그룹 게시판 ID가 유효하지 않습니다.");
        String memberId = pathParts[3];

        validateActivityGroupExist(activityGroupId);
        validateIsMemberPartOfActivity(memberId, activityGroupId);
        ActivityGroupBoard activityGroupBoard = activityGroupBoardService.getActivityGroupBoardById(
            activityGroupBoardId);
        validateIsParentBoardAssignment(activityGroupBoard);
    }

    private void validateActivityGroupExist(Long activityGroupId) {
        if (!activityGroupRepository.existsById(activityGroupId)) {
            throw new BaseException(ErrorCode.NOT_FOUND, "해당 활동은 존재하지 않습니다.");
        }
    }

    private void validateIsMemberPartOfActivity(String memberId, Long activityGroupId) {
        if (!groupMemberRepository.existsByMemberIdAndActivityGroupId(memberId, activityGroupId)) {
            throw new BaseException(ErrorCode.MEMBER_NOT_PART_OF_ACTIVITY_GROUP);
        }
    }

    private void validateIsMemberGroupLeader(Long activityGroupId, String memberId, String exceptionMessage) {
        if (!activityGroupAdminService.isMemberGroupLeaderRole(activityGroupId, memberId)) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, exceptionMessage);
        }
    }

    private void validateIsParentBoardAssignment(ActivityGroupBoard activityGroupBoard) {
        if (!activityGroupBoard.isAssignment()) {
            throw new BaseException(ErrorCode.INVALID_PARENT_BOARD, "부모 게시판이 ASSIGNMENT가 아닙니다.");
        }
    }

    private Long parseId(String idStr, String errorMessage) {
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new BaseException(ErrorCode.INVALID_PATH_VARIABLE, errorMessage);
        }
    }

    private void validateMemberCloudUsage(MultipartFile multipartFile, String path) {
        if (path.split(Pattern.quote(File.separator))[0].equals("members")) {
            String memberId = path.split(Pattern.quote(File.separator))[1];
            double usage = externalRetrieveCloudUsageByMemberIdUseCase.retrieveCloudUsage(memberId).getUsage();

            if (multipartFile.getSize() + usage > FileSystemUtil.convertToBytes(maxFileSize)) {
                throw new BaseException(ErrorCode.INSUFFICIENT_CLOUD_STORAGE);
            }
        }
    }

    private void checkAndRemoveExistingFile(String path) {
        List<String> validPrefixes = Arrays.asList("profiles", "members/", "assignments", "executives");
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
        if (category == null || category.isEmpty()) {
            return false;
        }
        return isUserAccessibleByCategory(category, url, authentication);
    }

    public boolean isUserAccessibleByCategory(String category, String url, Authentication authentication) {
        if (category.equals("activity-photos") || category.equals("executives")) {
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

    private boolean isNonSubmitCategoryAccessible(String url, Authentication authentication) {
        String memberId = authentication.getName();
        Member member = externalRetrieveMemberUseCase.getById(memberId);
        String[] parts = url.split("/");
        Long activityGroupId = Long.parseLong(parts[4]);

        return member.isSuperAdminRole() ||
            groupMemberRepository.existsByActivityGroupIdAndMemberIdAndStatus(activityGroupId, memberId,
                GroupMemberStatus.ACCEPTED);
    }

    private boolean isMemberAccessible(String url, Authentication authentication) {
        UploadedFile uploadedFile = uploadedFileService.getUploadedFileByUrl(url);
        String uploaderId = uploadedFile.getUploader();
        return authentication.getName().equals(uploaderId);
    }

    private boolean isSubmitAccessible(String url, Authentication authentication) {
        UploadedFile uploadedFile = uploadedFileService.getUploadedFileByUrl(url);
        String uploaderId = uploadedFile.getUploader();
        String memberId = authentication.getName();
        Member member = externalRetrieveMemberUseCase.getById(memberId);
        String[] parts = url.split("/");
        Long activityGroupId = Long.parseLong(parts[4]);

        return memberId.equals(uploaderId) || member.isSuperAdminRole() ||
            activityGroupAdminService.isMemberGroupLeaderRole(activityGroupId, authentication.getName());
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

