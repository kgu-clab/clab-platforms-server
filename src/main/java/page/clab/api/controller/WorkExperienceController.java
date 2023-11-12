package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
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
import page.clab.api.service.WorkExperienceService;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.dto.WorkExperienceRequestDto;
import page.clab.api.type.dto.WorkExperienceResponseDto;

@RestController
@RequestMapping("/work-experiences")
@RequiredArgsConstructor
@Tag(name = "WorkExperience", description = "경력사항 관련 API")
@Slf4j
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;

    @Operation(summary = "[U] 경력사항 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createWorkExperience(
            @Valid @RequestBody WorkExperienceRequestDto workExperienceRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        workExperienceService.createWorkExperience(workExperienceRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 나의 경력사항 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "입사일을 기준으로 내림차순 정렬하여 결과를 보여줌")
    @GetMapping("")
    public ResponseModel getMyWorkExperience() {
        List<WorkExperienceResponseDto> workExperienceResponseDtos = workExperienceService.getMyWorkExperience();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(workExperienceResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버의 경력사항 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "입사일을 기준으로 내림차순 정렬하여 결과를 보여줌")
    @GetMapping("/search")
    public ResponseModel searchWorkExperience(
            @RequestParam String memberId
    ) {
        List<WorkExperienceResponseDto> workExperienceResponseDtos = workExperienceService.searchWorkExperience(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(workExperienceResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 경력사항 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @PatchMapping("/{workExperienceId}")
    public ResponseModel updateWorkExperience(
            @PathVariable Long workExperienceId,
            @Valid @RequestBody WorkExperienceRequestDto workExperienceRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        workExperienceService.updateWorkExperience(workExperienceId, workExperienceRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 경력사항 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @DeleteMapping("/{workExperienceId}")
    public ResponseModel deleteWorkExperience(
            @PathVariable Long workExperienceId
    ) throws PermissionDeniedException {
        workExperienceService.deleteWorkExperience(workExperienceId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
