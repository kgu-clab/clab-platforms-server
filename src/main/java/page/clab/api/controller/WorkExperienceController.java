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
import page.clab.api.service.WorkExperienceService;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.dto.WorkExperienceRequestDto;
import page.clab.api.type.dto.WorkExperienceResponseDto;

import java.util.List;

@RestController
@RequestMapping("/work-experiences")
@RequiredArgsConstructor
@Tag(name = "WorkExperience")
@Slf4j
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;

    @Operation(summary = "경력사항 등록", description = "경력사항 등록")
    @PostMapping("")
    public ResponseModel createWorkExperience(
            @RequestBody WorkExperienceRequestDto workExperienceRequestDto
    ) {
        workExperienceService.createWorkExperience(workExperienceRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "내 경력사항 조회", description = "내 경력사항 조회<br>" +
            "입사일을 기준으로 내림차순 정렬하여 결과를 보여줌")
    @GetMapping("")
    public ResponseModel getMyWorkExperience() {
        List<WorkExperienceResponseDto> workExperienceResponseDtos = workExperienceService.getMyWorkExperience();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(workExperienceResponseDtos);
        return responseModel;
    }

    @Operation(summary = "멤버의 경력사항 검색", description = "멤버의 경력사항 검색<br>" +
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

    @Operation(summary = "경력사항 수정", description = "경력사항 수정")
    @PatchMapping("/{workExperienceId}")
    public ResponseModel updateWorkExperience(
            @PathVariable Long workExperienceId,
            @RequestBody WorkExperienceRequestDto workExperienceRequestDto
    ) throws PermissionDeniedException {
        workExperienceService.updateWorkExperience(workExperienceId, workExperienceRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "경력사항 삭제", description = "경력사항 삭제")
    @DeleteMapping("/{workExperienceId}")
    public ResponseModel deleteWorkExperience(
            @PathVariable Long workExperienceId
    ) throws PermissionDeniedException {
        workExperienceService.deleteWorkExperience(workExperienceId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
