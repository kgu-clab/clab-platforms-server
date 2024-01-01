package page.clab.api.service;

import com.google.zxing.WriterException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.AttendanceRepository;
import page.clab.api.type.dto.AttendanceRequestDto;
import page.clab.api.type.entity.Attendance;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.util.QRCodeUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final MemberService memberService;

    private final ActivityGroupAdminService activityGroupAdminService;

    private final AttendanceRepository attendanceRepository;

    public byte[] generateAttendanceQRCode(Long activityGroupId) throws IOException, WriterException, PermissionDeniedException {
        Member member = memberService.getCurrentMember();

        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, activityGroupId)) {
            throw new PermissionDeniedException("해당 그룹의 LEADER만 출석체크 QR을 생성할 수 있습니다.");
        }

        String data = activityGroupId.toString() + "/" + member.getId() + "/" + getCurrentTimestamp();

        Attendance attendance = Attendance.of(data, member);
        save(attendance);

        return QRCodeUtil.encodeQRCode(data);
    }

    public String checkMemberAttendance(AttendanceRequestDto attendanceRequestDto) {
        Member member = memberService.getCurrentMember();

        LocalDateTime enterTime = LocalDateTime.now();

        try {
            String base64EncodedData = attendanceRequestDto.getQRCodeData();
            byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedData);

            String QRCodeData = QRCodeUtil.decodeQRCode(decodedBytes);

            String[] QRCodeTokens = QRCodeData.split("/");

            if (QRCodeTokens.length != 3) {
                return "QRCode 정보가 잘못 되었습니다.";
            }

            Long activityGroupId = Long.parseLong(QRCodeTokens[0]);

            String QRCodeGeneratedTime = QRCodeTokens[2];

            if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.MEMBER, activityGroupId)) {
                return "해당 그룹의 멤버가 아닙니다. 출석체크 인증을 진행할 수 없습니다.";
            }

            if (!isValidQRCode(QRCodeGeneratedTime, enterTime)) {
                return "QRCode의 유효시간이 지났습니다. 출석체크 처리가 되지 않았습니다.";
            }

            Attendance attendance = Attendance.of(QRCodeData, member);
            Member attendancedMember = save(attendance).getAttendanceId().getMember();

            return attendancedMember.getId() + " " + attendancedMember.getName() + " "  + "출석체크 성공";

        } catch (Exception e) {
            e.printStackTrace();
            return "QRCode 디코딩 실패";
        }
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();

        return now.format(formatter);
    }

    private static boolean isValidQRCode(String QRCodeGeneratedTime, LocalDateTime enterTime){
        LocalDateTime QRCodeGeneratedAt = LocalDateTime.parse(QRCodeGeneratedTime, formatter);

        return java.time.Duration.between(QRCodeGeneratedAt, enterTime).getSeconds() <= 30;
    }

    public Attendance save(Attendance attendance){
        return attendanceRepository.save(attendance);
    }

}
