package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

import javax.validation.Valid;
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
            @Valid @RequestBody AwardRequestDto awardRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        awardService.createAward(awardRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "내 수상 이력 조회", description = "수상 이력 조회")
    @GetMapping("")
    public ResponseModel getMyAwards() {
        List<AwardResponseDto> awardResponseDtos = awardService.getMyAwards();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(awardResponseDtos);
        return responseModel;
    }

    @Operation(summary = "수상 이력 검색", description = "학번을 기준으로 검색")
    @GetMapping("/search")
    public ResponseModel searchAwards(
            @RequestParam String memberId
    ) {
        List<AwardResponseDto> awardResponseDtos = awardService.searchAwards(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(awardResponseDtos);
        return responseModel;
    }

  @Operation(summary = "수상 이력 수정", description = "수상 이력 수정")
  @PatchMapping("/{awardId}")
  public ResponseModel updateAward(
      @PathVariable Long awardId,
      @Valid @RequestBody AwardRequestDto awardRequestDto,
      BindingResult result
  ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
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
