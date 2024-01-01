package page.clab.api.controller;

import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.AttendanceService;
import page.clab.api.type.dto.AttendanceRequestDto;
import page.clab.api.type.dto.ResponseModel;

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
            @RequestParam Long activityGroupId
    ) throws IOException, WriterException, PermissionDeniedException {
        byte[] QRCode = attendanceService.generateAttendanceQRCode(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(QRCode);
        return responseModel;
    }

    @Operation(summary = "[U] 출석 인증", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/check-in")
    public ResponseModel checkInAttendance(
            @RequestBody AttendanceRequestDto attendanceRequestDto
    ){
        String id = attendanceService.checkMemberAttendance(attendanceRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
