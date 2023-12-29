package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.service.RecruitmentService;
import page.clab.api.type.dto.RecruitmentRequestDto;
import page.clab.api.type.dto.RecruitmentResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/recruitments")
@RequiredArgsConstructor
@Tag(name = "Recruitment", description = "모집 공고 관련 API")
@Slf4j
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @Operation(summary = "[A] 모집 공고 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createRecruitment(
            @Valid @RequestBody RecruitmentRequestDto recruitmentRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = recruitmentService.createRecruitment(recruitmentRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "모집 공고 목록(최근 5건)", description = "ROLE_ANONYMOUS 이상의 권한이 필요함<br>" +
            "최근 5건의 모집 공고를 조회")
    @GetMapping("")
    public ResponseModel getRecentRecruitments() {
        List<RecruitmentResponseDto> recruitments = recruitmentService.getRecentRecruitments();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(recruitments);
        return responseModel;
    }

    @Operation(summary = "[A] 모집 공고 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{recruitmentId}")
    public ResponseModel updateRecruitment(
            @PathVariable Long recruitmentId,
            @Valid @RequestBody RecruitmentRequestDto recruitmentRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = recruitmentService.updateRecruitment(recruitmentId, recruitmentRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 모집 공고 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{recruitmentId}")
    public ResponseModel deleteRecruitment(
            @PathVariable Long recruitmentId
    ) {
        Long id = recruitmentService.deleteRecruitment(recruitmentId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
