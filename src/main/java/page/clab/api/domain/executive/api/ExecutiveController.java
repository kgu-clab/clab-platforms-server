package page.clab.api.domain.executive.api;

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
import page.clab.api.domain.executive.application.ExecutiveService;
import page.clab.api.domain.executive.dto.request.ExecutiveRequestDto;
import page.clab.api.domain.executive.dto.response.ExecutiveResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/executives")
@RequiredArgsConstructor
@Tag(name = "Executive", description = "역대 운영진 관련 API")
@Slf4j
public class ExecutiveController {

    private final ExecutiveService executiveService;

    @Operation(summary = "운영진 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createExecutive(
            @Valid @RequestBody ExecutiveRequestDto executiveRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = executiveService.createExecutive(executiveRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "역대 운영진 목록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getExecutives(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ExecutiveResponseDto> executives = executiveService.getExecutives(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(executives);
        return responseModel;
    }

    @Operation(summary = "연도별 운영진 목록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{year}")
    public ResponseModel getExecutivesByYear(
            @PathVariable(name = "year") String year,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ExecutiveResponseDto> executives = executiveService.getExecutivesByYear(pageable, year);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(executives);
        return responseModel;
    }

    @Operation(summary = "역대 운영진 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{executiveId}")
    public ResponseModel deleteExecutive(
            @PathVariable(name = "executiveId") Long executiveId
    ) {
        Long id = executiveService.deleteExecutive(executiveId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
