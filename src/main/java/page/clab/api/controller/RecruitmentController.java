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
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.RecruitmentService;
import page.clab.api.type.dto.RecruitmentRequestDto;
import page.clab.api.type.dto.RecruitmentResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/recruitments")
@RequiredArgsConstructor
@Tag(name = "Recruitment")
@Slf4j
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @Operation(summary = "모집 공고 등록", description = "모집 공고 등록")
    @PostMapping("")
    public ResponseModel createRecruitment(
            @Valid @RequestBody RecruitmentRequestDto recruitmentRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        recruitmentService.createRecruitment(recruitmentRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "모집 공고 목록(최근 5건)", description = "모집 공고 목록(최근 5건)")
    @GetMapping("")
    public ResponseModel getRecentRecruitments() {
        List<RecruitmentResponseDto> recruitments = recruitmentService.getRecentRecruitments();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(recruitments);
        return responseModel;
    }

    @Operation(summary = "모집 공고 수정", description = "모집 공고 수정")
    @PatchMapping("/{recruitmentId}")
    public ResponseModel updateRecruitment(
            @PathVariable Long recruitmentId,
            @Valid @RequestBody RecruitmentRequestDto recruitmentRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        recruitmentService.updateRecruitment(recruitmentId, recruitmentRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "모집 공고 삭제", description = "모집 공고 삭제")
    @DeleteMapping("/{recruitmentId}")
    public ResponseModel deleteRecruitment(
            @PathVariable Long recruitmentId
    ) throws PermissionDeniedException {
        recruitmentService.deleteRecruitment(recruitmentId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
