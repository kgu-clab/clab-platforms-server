package page.clab.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.ScheduleService;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.dto.ScheduleRequestDto;
import page.clab.api.type.dto.ScheduleResponseDto;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "일정 관련 API")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "[U] 일정 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createSchedule(
            @Valid @RequestBody ScheduleRequestDto scheduleRequestDto
    ) throws PermissionDeniedException {
        Long id = scheduleService.createSchedule(scheduleRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 일정 조회", description = "ROLE_USER 이상의 권한이 필요함, 날짜시간은 다음과 같이 작성. yyyy-MM-dd HH:mm:ss.SSS")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getSchedules(
            @RequestParam(name = "start_date_time") String startDateTime,
            @RequestParam(name = "end_date_time") String endDateTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ScheduleResponseDto> scheduleResponseDtos
                = scheduleService.getSchedules(startDateTime, endDateTime, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(scheduleResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[A] 일정 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{scheduleId}")
    public ResponseModel deleteSchedule(
            @PathVariable Long scheduleId
    ) throws PermissionDeniedException {
        Long id = scheduleService.deleteSchedule(scheduleId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
