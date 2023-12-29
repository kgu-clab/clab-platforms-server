package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import page.clab.api.service.ActivityGroupBoardService;
import page.clab.api.type.dto.ActivityGroupBoardChildResponseDto;
import page.clab.api.type.dto.ActivityGroupBoardRequestDto;
import page.clab.api.type.dto.ActivityGroupBoardResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;

import javax.validation.Valid;

@RestController
@RequestMapping("/activity-group/boards")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupBoard", description = "활동 그룹 게시판 관리 관련 API")
@Slf4j
public class ActivityGroupBoardController {

    private final ActivityGroupBoardService activityGroupBoardService;

    @Operation(summary = "[U] 활동 그룹 게시판 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createActivityGroupBoard(
            @RequestParam(required = false) Long parentId,
            @RequestParam Long activityGroupId,
            @Valid @RequestBody ActivityGroupBoardRequestDto activityGroupBoardRequestDto
    ) {
        Long id = activityGroupBoardService.createActivityGroupBoard(parentId, activityGroupId, activityGroupBoardRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 그룹 게시판 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/list")
    public ResponseModel getActivityGroupBoardList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        PagedResponseDto<ActivityGroupBoardResponseDto> allBoards = activityGroupBoardService.getAllActivityGroupBoard(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(allBoards);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 그룹 게시판 단일 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getActivityGroupBoardById(
            @RequestParam Long activityGroupBoardId
    ) {
        ActivityGroupBoardResponseDto board = activityGroupBoardService.getActivityGroupBoardById(activityGroupBoardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(board);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 그룹 게시판 계층 구조적 조회, 부모 및 자식 게시판 함께 반환", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/by-parent")
    public ResponseModel getActivityGroupBoardByParent(
            @RequestParam Long parentId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        PagedResponseDto<ActivityGroupBoardChildResponseDto> boards = activityGroupBoardService.getActivityGroupBoardByParent(parentId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(boards);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 그룹 게시판 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("")
    public ResponseModel updateActivityGroupBoard(
            @RequestParam Long activityGroupBoardId,
            @Valid @RequestBody ActivityGroupBoardRequestDto activityGroupBoardRequestDto
    ) {
        Long id = activityGroupBoardService.updateActivityGroupBoard(activityGroupBoardId, activityGroupBoardRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 그룹 게시판 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("")
    public ResponseModel deleteActivityGroupBoard(
            @RequestParam Long activityGroupBoardId
    ) {
        Long id = activityGroupBoardService.deleteActivityGroupBoard(activityGroupBoardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}