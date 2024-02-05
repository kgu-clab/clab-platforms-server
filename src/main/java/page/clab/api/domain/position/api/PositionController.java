package page.clab.api.domain.position.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.position.application.PositionService;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.domain.position.dto.request.PositionRequestDto;
import page.clab.api.domain.position.dto.response.PositionMyResponseDto;
import page.clab.api.domain.position.dto.response.PositionResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
@Tag(name = "Position", description = "멤버 직책 관련 API")
@Slf4j
public class PositionController {

    private final PositionService positionService;

    @Operation(summary = "직책 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createPosition(
            @Valid @RequestBody PositionRequestDto positionRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = positionService.createPosition(positionRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "연도/직책별 목록 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "연도와 직책을 선택하여 조회할 수 있음<br>" +
            "연도와 직책을 선택하지 않으면 전체 목록을 조회함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel searchPositions(
            @RequestParam(name = "year", required = false) String year,
            @RequestParam(name = "positionType", required = false) PositionType positionType,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<PositionResponseDto> positions = positionService.searchPositions(year, positionType, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(positions);
        return responseModel;
    }

    @Operation(summary = "나의 직책 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my-positions")
    public ResponseModel getMyPositionsByYear(
            @RequestParam(name = "year", required = false) String year
    ) {
        PositionMyResponseDto positions = positionService.getMyPositionsByYear(year);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(positions);
        return responseModel;
    }

    @Operation(summary = "직책 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{positionId}")
    public ResponseModel deletePosition(
            @PathVariable("positionId") Long positionId
    ) {
        Long id = positionService.deletePosition(positionId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}