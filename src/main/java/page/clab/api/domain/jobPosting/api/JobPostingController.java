package page.clab.api.domain.jobPosting.api;

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
import page.clab.api.domain.jobPosting.application.JobPostingService;
import page.clab.api.domain.jobPosting.dto.request.JobPostingRequestDto;
import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.domain.jobPosting.dto.response.JobPostingResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/job-postings")
@RequiredArgsConstructor
@Tag(name = "JobPosting", description = "채용 공고")
@Slf4j
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @Operation(summary = "[A] 채용 공고 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createJobPosting(
            @Valid @RequestBody JobPostingRequestDto jobPostingRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = jobPostingService.createJobPosting(jobPostingRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 채용 공고 목록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getJobPostings(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<JobPostingResponseDto> jobPostings = jobPostingService.getJobPostings(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(jobPostings);
        return responseModel;
    }

    @Operation(summary = "[U] 채용 공고 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{jobPostingId}")
    public ResponseModel getJobPosting(
            @PathVariable(name = "jobPostingId") Long jobPostingId
    ) {
        JobPostingDetailsResponseDto jobPosting = jobPostingService.getJobPosting(jobPostingId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(jobPosting);
        return responseModel;
    }

    @Operation(summary = "[A] 채용 공고 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "공고명, 기업명을 기준으로 검색")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/search")
    public ResponseModel searchJobPostings(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<JobPostingResponseDto> jobPostings = jobPostingService.searchJobPostings(keyword, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(jobPostings);
        return responseModel;
    }

    @Operation(summary = "[A] 채용 공고 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/{jobPostingId}")
    public ResponseModel updateJobPosting(
            @PathVariable(name = "jobPostingId") Long jobPostingId,
            @Valid @RequestBody JobPostingRequestDto jobPostingRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = jobPostingService.updateJobPosting(jobPostingId, jobPostingRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 채용 공고 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{jobPostingId}")
    public ResponseModel deleteJobPosting(
            @PathVariable(name = "jobPostingId") Long jobPostingId
    ) {
        Long id = jobPostingService.deleteJobPosting(jobPostingId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
