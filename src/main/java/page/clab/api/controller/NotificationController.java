package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.NotificationService;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.NotificationResponseDto;
import page.clab.api.type.dto.ResponseModel;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 생성", description = "알림 생성<br>" +
            "String memberId<br>" +
            "String content;")
    @PostMapping("")
    public ResponseModel createNotification(
            @RequestBody NotificationRequestDto notificationRequestDto
    ) {
        notificationService.createNotification(notificationRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "알림 조회", description = "알림 조회")
    @GetMapping("")
    public ResponseModel getNotifications() {
        List<NotificationResponseDto> notifications = notificationService.getNotifications();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(notifications);
        return responseModel;
    }

    @Operation(summary = "알림 삭제", description = "알림 삭제")
    @DeleteMapping("/{notificationId}")
    public ResponseModel deleteNotification(
            @PathVariable("notificationId") Long notificationId
    ) throws PermissionDeniedException {
        notificationService.deleteNotification(notificationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
