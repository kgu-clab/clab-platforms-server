package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.ExecutivesService;
import page.clab.api.type.dto.ExecutivesRequestDto;
import page.clab.api.type.dto.ExecutivesResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/executives")
@RequiredArgsConstructor
@Tag(name = "Executives", description = "역대 운영진 관련 API")
@Slf4j
public class ExecutivesController {

    private final ExecutivesService executivesService;

    @Operation(summary = "운영진 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createExecutives(
            @Valid @RequestBody ExecutivesRequestDto executivesRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        executivesService.createExecutives(executivesRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "역대 운영진 목록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getExecutives(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<ExecutivesResponseDto> executives = executivesService.getExecutives(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(executives);
        return responseModel;
    }

    @Operation(summary = "연도별 운영진 목록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/{year}")
    public ResponseModel getExecutivesByYear(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable String year
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<ExecutivesResponseDto> executives = executivesService.getExecutivesByYear(pageable, year);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(executives);
        return responseModel;
    }

    @Operation(summary = "역대 운영진 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @DeleteMapping("/{executivesId}")
    public ResponseModel deleteExecutives(
            @PathVariable Long executivesId
    ) throws PermissionDeniedException {
        executivesService.deleteExecutives(executivesId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
