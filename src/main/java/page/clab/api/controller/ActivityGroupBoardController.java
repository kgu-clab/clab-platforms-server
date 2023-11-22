package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.service.ActivityGroupBoardService;
import page.clab.api.type.dto.ActivityGroupBoardDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/activity-group/boards")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupBoard", description = "활동 그룹 게시판 관리 관련 API")
@Slf4j
public class ActivityGroupBoardController {

    private final ActivityGroupBoardService activityGroupBoardService;

    @Operation(summary = "[U] 활동 그룹 게시판 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createActivityGroupBoard(
            @RequestParam(required = false) Long parentId,
            @RequestParam Long activityGroupId,
            @RequestBody ActivityGroupBoardDto activityGroupBoardDto
    ) {
        activityGroupBoardService.createActivityGroupBoard(parentId, activityGroupId, activityGroupBoardDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 활동 그룹 게시판 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/list")
    public ResponseModel getActivityGroupBoardList() {
        List<ActivityGroupBoardDto> allBoards = activityGroupBoardService.getAllActivityGroupBoard();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(allBoards);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 그룹 게시판 단일 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getActivityGroupBoardById(
            @RequestParam Long activityGroupBoardId
    ) {
        ActivityGroupBoardDto board = activityGroupBoardService.getActivityGroupBoardById(activityGroupBoardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(board);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 그룹 게시판 계층 구조적 조회, 부모 및 자식 게시판 함께 반환", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/by-parent")
    public ResponseModel getActivityGroupBoardByParent(
            @RequestParam Long parentId
    ) {
        List<ActivityGroupBoardDto> boards = activityGroupBoardService.getActivityGroupBoardByParent(parentId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(boards);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 그룹 게시판 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @PatchMapping("")
    public ResponseModel updateActivityGroupBoard(
            @RequestParam Long activityGroupBoardId,
            @RequestBody ActivityGroupBoardDto activityGroupBoardDto
    ) {
        activityGroupBoardService.updateActivityGroupBoard(activityGroupBoardId, activityGroupBoardDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 활동 그룹 게시판 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @DeleteMapping("")
    public ResponseModel deleteActivityGroupBoard(
            @RequestParam Long activityGroupBoardId
    ) {
        activityGroupBoardService.deleteActivityGroupBoard(activityGroupBoardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
