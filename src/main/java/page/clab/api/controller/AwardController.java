package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.AwardService;
import page.clab.api.type.dto.AwardRequestDto;
import page.clab.api.type.dto.AwardResponseDto;
import page.clab.api.type.dto.ResponseModel;

import java.util.List;

@RestController
@RequestMapping("/award")
@RequiredArgsConstructor
@Tag(name = "Award")
@Slf4j
public class AwardController {

    private final AwardService awardService;

    @Operation(summary = "수상 이력 등록", description = "수상 이력 등록")
    @PostMapping("")
    public ResponseModel createAward(
            @RequestBody AwardRequestDto awardRequestDto
    ) {
        awardService.createAward(awardRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "수상 이력 조회", description = "수상 이력 조회")
    @GetMapping("")
    public ResponseModel getAwards() {
        List<AwardResponseDto> awardResponseDtos = awardService.getAwards();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(awardResponseDtos);
        return responseModel;
    }

    @Operation(summary = "수상 이력 검색", description = "대회명, 주최, 상장명, 참가 멤버를 기준으로 검색")
    @GetMapping("/search")
    public ResponseModel searchAwards(
            @RequestParam String keyword
    ) {
        List<AwardResponseDto> awardResponseDtos = awardService.searchAwards(keyword);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(awardResponseDtos);
        return responseModel;
    }

    @Operation(summary = "수상 이력 수정", description = "수상 이력 수정")
    @PatchMapping("/{awardId}")
    public ResponseModel updateAward(
            @PathVariable Long awardId,
            @RequestBody AwardRequestDto awardRequestDto
    ) throws PermissionDeniedException {
        awardService.updateAward(awardId, awardRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "수상 이력 삭제", description = "수상 이력 삭제")
    @DeleteMapping("/{awardId}")
    public ResponseModel deleteAward(
            @PathVariable Long awardId
    ) throws PermissionDeniedException {
        awardService.deleteAward(awardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
