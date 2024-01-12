package page.clab.api.service;

import com.google.zxing.WriterException;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.AttendanceRepository;
import page.clab.api.repository.RedisQRKeyRepository;
import page.clab.api.type.dto.AttendanceRequestDto;
import page.clab.api.type.dto.AttendanceResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.Attendance;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.RedisQRKey;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.util.QRCodeUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final MemberService memberService;

    private final FileService fileService;

    private final ActivityGroupAdminService activityGroupAdminService;

    private final AttendanceRepository attendanceRepository;

    private final RedisQRKeyRepository redisQRKeyRepository;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String generateAttendanceQRCode(Long activityGroupId) throws IOException, WriterException, PermissionDeniedException, IllegalAccessException {
        Member member = memberService.getCurrentMember();

        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);

        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, activityGroupId)) {
            throw new PermissionDeniedException("해당 그룹의 LEADER만 출석체크 QR을 생성할 수 있습니다.");
        }

        if (!activityGroupAdminService.isActivityGroupProgressing(activityGroupId)) {
            throw new IllegalAccessException("활동이 진행 중인 그룹이 아닙니다. 출석 QR 코드를 생성할 수 없습니다.");
        }

        String nowDateTime = getCurrentTimestamp();

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secretKey = key.getKey();
        RedisQRKey redisQRKey = RedisQRKey.builder().QRCodeKey(secretKey).build();
        redisQRKeyRepository.save(redisQRKey);

        String url = "clab.page/attendance?activityGroupId=" + activityGroupId.toString() + "&secretKey=" + secretKey;

        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setActivityGroup(activityGroup);
        attendance.setActivityDate(LocalDate.parse(nowDateTime.split(" ")[0], dateFormatter));
        save(attendance);

        byte[] QRCodeImage = QRCodeUtil.encodeQRCode(url);

        String path = "attendance" + File.separator
                + activityGroup.getCategory().toString() + File.separator
                + activityGroup.getId().toString();
        String fileUrl = fileService.saveQRCodeImage(QRCodeImage, path, 1, nowDateTime);

        return url;
    }

    public String checkMemberAttendance(AttendanceRequestDto attendanceRequestDto) {
        Member member = memberService.getCurrentMember();
        Long activityGroupId = attendanceRequestDto.getActivityGroupId();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.MEMBER, activityGroupId)) {
            return "해당 그룹의 멤버가 아닙니다. 출석체크 인증을 진행할 수 없습니다.";
        }

        LocalDateTime enterTime = LocalDateTime.now();

        String QRCodeData  = attendanceRequestDto.getQRCodeSecretKey();
        if (QRCodeData == null) {
            return "QRCode 정보가 잘못 되었습니다.";
        }

        RedisQRKey redisQRKey = redisQRKeyRepository.findByQRCodeKey(QRCodeData)
                .orElseThrow(() -> new NotFoundException("QRCode 정보가 없습니다."));

        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setActivityGroup(activityGroup);
        attendance.setActivityDate(enterTime.toLocalDate());
        Member attendancedMember = save(attendance).getMember();

        return attendancedMember.getId() + " " + attendancedMember.getName() + " "  + "출석체크 성공";
    }

    public PagedResponseDto<AttendanceResponseDto> getMyAttendances(Long activityGroupId, Pageable pageable) throws IllegalAccessException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);

        if (!activityGroupAdminService.isActivityGroupProgressing(activityGroupId)) {
            throw new IllegalAccessException("활동이 진행 중인 그룹이 아닙니다. 출석 정보를 불러올 수 없습니다.");
        }

        Page<Attendance> attendances = getAttendanceByMember(pageable, member, activityGroup);

        return new PagedResponseDto<>(attendances.map(AttendanceResponseDto::of));
    }

    public PagedResponseDto<AttendanceResponseDto> getGroupAttendances(Long activityGroupId, Pageable pageable) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);

        if (!memberService.isMemberAdminRole(member) || !activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, activityGroupId)) {
            throw new PermissionDeniedException("그룹의 출석기록을 조회할 권한이 없습니다.");
        }

        Page<Attendance> attendances = getAttendanceByActivityGroup(pageable, activityGroup);

        return new PagedResponseDto<>(attendances.map(AttendanceResponseDto::of));
    }

    private static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();

        return now.format(dateTimeFormatter);
    }

    private Page<Attendance> getAttendanceByMember(Pageable pageable, Member member, ActivityGroup activityGroup){
        return attendanceRepository.findAllByMemberAndActivityGroupOrderByCreatedAt(member, activityGroup, pageable);
    }

    private Page<Attendance> getAttendanceByActivityGroup(Pageable pageable, ActivityGroup activityGroup){
        return attendanceRepository.findAllByActivityGroupOrderByActivityDateAscMemberAsc(activityGroup, pageable);
    }

    public Attendance save(Attendance attendance){
        return attendanceRepository.save(attendance);
    }

}
