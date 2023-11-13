package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.service.AccuseService;
import page.clab.api.type.dto.AccuseDto;
import page.clab.api.type.dto.ResponseModel;

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
        List<AccuseDto> accuses = accuseService.getAccuses();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(accuses);
        return responseModel;
    }

    @Operation(summary = "[A] 신고 내역 초기화", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @DeleteMapping("")
    public ResponseModel deleteAccuse(
            @RequestParam Long id
    ) {
        accuseService.deleteAccuse(id);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/member")
    public ResponseModel memberAccuse(
            @RequestParam String memberId
    ) {
        accuseService.memberAccuse(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 게시글 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/board")
    public ResponseModel boardAccuse(
            @RequestParam Long boardId
    ) {
        accuseService.boardAccuse(boardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 댓글 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/comment")
    public ResponseModel commentAccuse(
            @RequestParam Long commentId
    ) {
        accuseService.commentAccuse(commentId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 블로그 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/blog")
    public ResponseModel blogAccuse(
            @RequestParam Long blogId
    ) {
        accuseService.blogAccuse(blogId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 뉴스 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/news")
    public ResponseModel newsAccuse(
            @RequestParam Long newsId
    ) {
        accuseService.newsAccuse(newsId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 리뷰 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/review")
    public ResponseModel reviewAccuse(
            @RequestParam Long reviewId
    ) {
        accuseService.reviewAccuse(reviewId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
