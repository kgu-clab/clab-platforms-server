package page.clab.api.domain.activityGroup.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activityGroup.application.ActivityGroupBoardService;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupBoardRequestDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupBoardUpdateRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardChildResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardUpdateResponseDto;
import page.clab.api.domain.activityGroup.dto.response.AssignmentSubmissionWithFeedbackResponseDto;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity-group/boards")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupBoard", description = "활동 그룹 게시판 관리")
@Slf4j
public class ActivityGroupBoardController {

    private final ActivityGroupBoardService activityGroupBoardService;

    @Operation(summary = "[U] 활동 그룹 게시판 생성", description = "ROLE_USER 이상의 권한이 필요함<br><br>" +
            "활동 그룹 게시판 카테고리별 requestDto에 들어가야 할 필수내용과 (선택)내용입니다.<br><br>" +
            "공지사항, 주차별활동 : 카테고리, 제목, 내용, 첨부파일 경로 리스트(선택)<br>" +
            "과제 : 부모 게시판(주차별활동), 카테고리, 제목, 내용, 마감일자, 첨부파일 경로 리스트(선택)<br>" +
            "제출 : 부모 게시판(과제), 첨부파일 경로 리스트<br>" +
            "피드백 : 부모 게시판(제출), 카테고리, 내용 , 첨부파일 경로 리스트(선택)"
    )
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createActivityGroupBoard(
            @RequestParam(name = "parentId", required = false) Long parentId,
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @Valid @RequestBody ActivityGroupBoardRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = activityGroupBoardService.createActivityGroupBoard(parentId, activityGroupId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 활동 그룹 게시판 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/list")
    public ApiResponse<PagedResponseDto<ActivityGroupBoardResponseDto>> getActivityGroupBoardList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupBoardResponseDto> boards = activityGroupBoardService.getAllActivityGroupBoard(pageable);
        return ApiResponse.success(boards);
    }

    @Operation(summary = "[U] 활동 그룹 게시판 단일 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<ActivityGroupBoardResponseDto> getActivityGroupBoardById(
            @RequestParam(name = "activityGroupBoardId") Long activityGroupBoardId
    ) {
        ActivityGroupBoardResponseDto board = activityGroupBoardService.getActivityGroupBoardById(activityGroupBoardId);
        return ApiResponse.success(board);
    }

    @Operation(summary = "[U] 활동 그룹 ID에 대한 카테고리별 게시판 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/by-category")
    public ApiResponse<PagedResponseDto<ActivityGroupBoardResponseDto>> getActivityGroupBoardByCategory(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "category") ActivityGroupBoardCategory category,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupBoardResponseDto> boards = activityGroupBoardService.getActivityGroupBoardByCategory(activityGroupId, category, pageable);
        return ApiResponse.success(boards);
    }

    @Operation(summary = "[U] 활동 그룹 게시판 계층 구조적 조회, 부모 및 자식 게시판 함께 반환", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/by-parent")
    public ApiResponse<PagedResponseDto<ActivityGroupBoardChildResponseDto>> getActivityGroupBoardByParent(
            @RequestParam(name = "parentId") Long parentId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) throws PermissionDeniedException {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupBoardChildResponseDto> boards = activityGroupBoardService.getActivityGroupBoardByParent(parentId, pageable);
        return ApiResponse.success(boards);
    }

    @Operation(summary = "[U] 나의 제출 과제 및 피드백 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my-assignment")
    public ApiResponse<List<AssignmentSubmissionWithFeedbackResponseDto>> getMyAssignmentBoardWithFeedback(
            @RequestParam(name = "parentId") Long parentId
    ) {
        List<AssignmentSubmissionWithFeedbackResponseDto> boards = activityGroupBoardService.getMyAssignmentsWithFeedbacks(parentId);
        return ApiResponse.success(boards);
    }

    @Operation(summary = "[U] 활동 그룹 게시판 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("")
    public ApiResponse<ActivityGroupBoardUpdateResponseDto> updateActivityGroupBoard(
            @RequestParam(name = "activityGroupBoardId") Long activityGroupBoardId,
            @Valid @RequestBody ActivityGroupBoardUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        ActivityGroupBoardUpdateResponseDto board = activityGroupBoardService.updateActivityGroupBoard(activityGroupBoardId, requestDto);
        return ApiResponse.success(board);
    }

    @Operation(summary = "[U] 활동 그룹 게시판 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("")
    public ApiResponse<Long> deleteActivityGroupBoard(
            @RequestParam Long activityGroupBoardId
    ) throws PermissionDeniedException {
        Long id = activityGroupBoardService.deleteActivityGroupBoard(activityGroupBoardId);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 활동 그룹 게시판 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<ActivityGroupBoardResponseDto>> getDeletedActivityGroupBoards(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupBoardResponseDto> activityBoards = activityGroupBoardService.getDeletedActivityGroupBoards(pageable);
        return ApiResponse.success(activityBoards);
    }

}