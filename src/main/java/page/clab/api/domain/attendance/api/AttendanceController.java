package page.clab.api.domain.attendance.api;

import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activityGroup.dto.request.AbsentRequestDto;
import page.clab.api.domain.activityGroup.dto.response.AbsentResponseDto;
import page.clab.api.domain.activityGroup.exception.DuplicateAbsentExcuseException;
import page.clab.api.domain.attendance.application.AttendanceService;
import page.clab.api.domain.attendance.dto.request.AttendanceRequestDto;
import page.clab.api.domain.attendance.dto.response.AttendanceResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance", description = "출석체크 관련 API")
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "[U] 출석체크 QR 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping(value = "")
    public ResponseModel generateAttendanceQRCode (
            @RequestParam(name = "activityGroupId") Long activityGroupId
    ) throws IOException, WriterException, PermissionDeniedException, IllegalAccessException {
        String QRCodeURL = attendanceService.generateAttendanceQRCode(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(QRCodeURL);
        return responseModel;
    }

    @Operation(summary = "[U] 출석 인증", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/check-in")
    public ResponseModel checkInAttendance(
            @RequestBody AttendanceRequestDto attendanceRequestDto
    ) throws IllegalAccessException {
        String id = attendanceService.checkMemberAttendance(attendanceRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 내 출석기록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping({"/my-attendance"})
    public ResponseModel searchMyAttendance(
            @RequestParam(name = "activityGroupId", defaultValue = "1") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) throws IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AttendanceResponseDto> attendanceResponseDtos = attendanceService.getMyAttendances(activityGroupId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(attendanceResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 특정 그룹의 출석기록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping({"/group-attendance"})
    public ResponseModel searchGroupAttendance(
            @RequestParam(name = "activityGroupId", defaultValue = "1") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) throws PermissionDeniedException {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AttendanceResponseDto> attendanceResponseDtos = attendanceService.getGroupAttendances(activityGroupId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(attendanceResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 불참 사유서 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping({"/absent"})
    public ResponseModel writeAbsentExcuse(
            @RequestBody AbsentRequestDto absentRequestDto
    ) throws IllegalAccessException, DuplicateAbsentExcuseException {
        Long id = attendanceService.writeAbsentExcuse(absentRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 그룹의 불참 사유서 열람", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping({"/absent/{ActivityGroupId}"})
    public ResponseModel getActivityGroupAbsentExcuses(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) throws PermissionDeniedException {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AbsentResponseDto> absentExcuses = attendanceService.getActivityGroupAbsentExcuses(activityGroupId, pageable);
        ResponseModel responseModel =  ResponseModel.builder().build();
        responseModel.addData(absentExcuses);
        return responseModel;
    }

}
