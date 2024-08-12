package page.clab.api.domain.activity.activitygroup.api;

import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activity.activitygroup.application.AttendanceService;
import page.clab.api.domain.activity.activitygroup.dto.request.AbsentRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.AttendanceRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.response.AbsentResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.AttendanceResponseDto;
import page.clab.api.domain.activity.activitygroup.exception.DuplicateAbsentExcuseException;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@Tag(name = "Activity - Attendance", description = "활동 출석체크")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[U] 출석체크 QR 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "")
    public ApiResponse<String> generateAttendanceQRCode(
            @RequestParam(name = "activityGroupId") Long activityGroupId
    ) throws IOException, WriterException, PermissionDeniedException, IllegalAccessException {
        String QRCodeURL = attendanceService.generateAttendanceQRCode(activityGroupId);
        return ApiResponse.success(QRCodeURL);
    }

    @Operation(summary = "[U] 출석 인증", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/check-in")
    public ApiResponse<Long> checkInAttendance(
            @RequestBody AttendanceRequestDto requestDto
    ) throws IllegalAccessException {
        Long id = attendanceService.checkMemberAttendance(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[G] 내 출석기록 조회", description = "ROLE_GUEST 이상의 권한이 필요함<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping({ "/my-attendance" })
    public ApiResponse<PagedResponseDto<AttendanceResponseDto>> searchMyAttendance(
            @RequestParam(name = "activityGroupId", defaultValue = "1") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "attendanceDateTime") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, IllegalAccessException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, AttendanceResponseDto.class);
        PagedResponseDto<AttendanceResponseDto> myAttendances = attendanceService.getMyAttendances(activityGroupId, pageable);
        return ApiResponse.success(myAttendances);
    }

    @Operation(summary = "[U] 특정 그룹의 출석기록 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('USER')")
    @GetMapping({ "/group-attendance" })
    public ApiResponse<PagedResponseDto<AttendanceResponseDto>> searchGroupAttendance(
            @RequestParam(name = "activityGroupId", defaultValue = "1") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "memberId") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") List<String> sortDirection
    ) throws SortingArgumentException, PermissionDeniedException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, AttendanceResponseDto.class);
        PagedResponseDto<AttendanceResponseDto> attendances = attendanceService.getGroupAttendances(activityGroupId, pageable);
        return ApiResponse.success(attendances);
    }

    @Operation(summary = "[U] 불참 사유서 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping({ "/absent" })
    public ApiResponse<Long> writeAbsentExcuse(
            @RequestBody AbsentRequestDto requestDto
    ) throws IllegalAccessException, DuplicateAbsentExcuseException {
        Long id = attendanceService.writeAbsentExcuse(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 그룹의 불참 사유서 열람", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('USER')")
    @GetMapping({ "/absent/{activityGroupId}" })
    public ApiResponse<PagedResponseDto<AbsentResponseDto>> getActivityGroupAbsentExcuses(
            @PathVariable(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "absentDate") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, PermissionDeniedException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, AbsentResponseDto.class);
        PagedResponseDto<AbsentResponseDto> absentExcuses = attendanceService.getActivityGroupAbsentExcuses(activityGroupId, pageable);
        return ApiResponse.success(absentExcuses);
    }
}
