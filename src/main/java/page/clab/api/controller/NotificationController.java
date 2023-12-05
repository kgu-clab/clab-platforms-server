package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.NotificationService;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.NotificationResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림 관련 API")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "[U] 알림 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createNotification(
            @Valid @RequestBody NotificationRequestDto notificationRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = notificationService.createNotification(notificationRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 나의 알림 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<NotificationResponseDto> notifications = notificationService.getNotifications(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(notifications);
        return responseModel;
    }

    @Operation(summary = "[U] 알림 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @DeleteMapping("/{notificationId}")
    public ResponseModel deleteNotification(
            @PathVariable("notificationId") Long notificationId
    ) throws PermissionDeniedException {
        Long id = notificationService.deleteNotification(notificationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
