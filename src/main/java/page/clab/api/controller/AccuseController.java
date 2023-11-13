package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.service.AccuseService;
import page.clab.api.type.dto.AccuseRequestDto;
import page.clab.api.type.dto.AccuseResponseDto;
import page.clab.api.type.dto.ResponseModel;

import java.util.List;

@RestController
@RequestMapping("/accuses")
@RequiredArgsConstructor
@Tag(name = "Accuse", description = "신고 관련 API")
@Slf4j
public class AccuseController {

    private final AccuseService accuseService;

    @Operation(summary = "[A] 신고 내역 리스팅", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getAccuses() {
        List<AccuseResponseDto> accuses = accuseService.getAccuses();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(accuses);
        return responseModel;
    }

    @Operation(summary = "[A] 신고 내역 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @GetMapping("/target")
    public ResponseModel getAccuseByTargetId(
            @RequestParam Long targetId,
            @RequestParam String category
    ) {
        List<AccuseResponseDto> accuse = accuseService.getAccuseByTargetId(targetId, category);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(accuse);
        return responseModel;
    }

    @Operation(summary = "[A] 신고 내역 초기화", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @DeleteMapping("")
    public ResponseModel deleteAccuse(
            @RequestParam Long targetId,
            @RequestParam String category
    ) {
        accuseService.deleteAccuse(targetId, category);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/member")
    public ResponseModel memberAccuse(
            @RequestParam String memberId,
            @RequestBody AccuseRequestDto accuseRequestDto
    ) {
        accuseService.memberAccuse(memberId, accuseRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 게시글 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/board")
    public ResponseModel boardAccuse(
            @RequestParam Long boardId,
            @RequestBody AccuseRequestDto accuseRequestDto
    ) {
        accuseService.boardAccuse(boardId, accuseRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 댓글 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/comment")
    public ResponseModel commentAccuse(
            @RequestParam Long commentId,
            @RequestBody AccuseRequestDto accuseRequestDto
    ) {
        accuseService.commentAccuse(commentId, accuseRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 블로그 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/blog")
    public ResponseModel blogAccuse(
            @RequestParam Long blogId,
            @RequestBody AccuseRequestDto accuseRequestDto
    ) {
        accuseService.blogAccuse(blogId, accuseRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 뉴스 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/news")
    public ResponseModel newsAccuse(
            @RequestParam Long newsId,
            @RequestBody AccuseRequestDto accuseRequestDto
    ) {
        accuseService.newsAccuse(newsId, accuseRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 리뷰 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/review")
    public ResponseModel reviewAccuse(
            @RequestParam Long reviewId,
            @RequestBody AccuseRequestDto accuseRequestDto
    ) {
        accuseService.reviewAccuse(reviewId, accuseRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
