package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
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
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;

import javax.validation.Valid;

@RestController
@RequestMapping("/awards")
@RequiredArgsConstructor
@Tag(name = "Award", description = "수상 이력 관련 API")
@Slf4j
public class AwardController {

    private final AwardService awardService;

    @Operation(summary = "[U] 수상 이력 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createAward(
            @Valid @RequestBody AwardRequestDto awardRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = awardService.createAward(awardRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 나의 수상 이력 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getMyAwards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AwardResponseDto> awardResponseDtos = awardService.getMyAwards(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(awardResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 수상 이력 검색", description = "ROLE_USER 이상의 권한이 필요함" +
            "학번을 기준으로 검색")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/search")
    public ResponseModel searchAwards(
            @RequestParam String memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AwardResponseDto> awardResponseDtos = awardService.searchAwards(memberId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(awardResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 수상 이력 년도별 조회", description = "ROLE_USER 이상의 권한이 필요함" +
            "년도를 기준으로 검색")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/annual/search")
    public ResponseModel searchAnualAwards(
            @RequestParam String year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AwardResponseDto> awardResponseDtos = awardService.searchAnualAwards(year, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(awardResponseDtos);
        return responseModel;
    }

  @Operation(summary = "[U] 수상 이력 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
  @PatchMapping("/{awardId}")
  public ResponseModel updateAward(
      @PathVariable Long awardId,
      @Valid @RequestBody AwardRequestDto awardRequestDto,
      BindingResult result
  ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = awardService.updateAward(awardId, awardRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 수상 이력 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{awardId}")
    public ResponseModel deleteAward(
            @PathVariable Long awardId
    ) throws PermissionDeniedException {
        Long id = awardService.deleteAward(awardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
