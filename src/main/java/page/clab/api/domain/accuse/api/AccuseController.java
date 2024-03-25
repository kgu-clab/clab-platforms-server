package page.clab.api.domain.accuse.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.accuse.application.AccuseService;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;
import page.clab.api.domain.accuse.dto.response.AccuseResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/api/v1/accuses")
@RequiredArgsConstructor
@Tag(name = "Accuse", description = "신고")
@Slf4j
public class AccuseController {

    private final AccuseService accuseService;

    @Operation(summary = "[U] 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createAccuse(
            @Valid @RequestBody AccuseRequestDto requestDto
    ) {
        Long id = accuseService.createAccuse(requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 신고 내역 조회(신고 대상, 처리 상태 기준)", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "2개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "신고 대상, 처리 상태 중 하나라도 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getAccusesByConditions(
            @RequestParam(name = "targetType", required = false) TargetType type,
            @RequestParam(name = "accuseStatus", required = false) AccuseStatus status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AccuseResponseDto> accuses = accuseService.getAccusesByConditions(type, status, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(accuses);
        return responseModel;
    }

    @Operation(summary = "[A] 신고 상태 변경", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{accuseId}")
    public ResponseModel updateAccuseStatus(
            @PathVariable(name = "accuseId") Long accuseId,
            @RequestParam(name = "accuseStatus") AccuseStatus status
    ) {
        Long id = accuseService.updateAccuseStatus(accuseId, status);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
