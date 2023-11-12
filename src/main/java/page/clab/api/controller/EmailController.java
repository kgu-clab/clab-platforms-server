package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.EmailService;
import page.clab.api.type.dto.EmailDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
@Tag(name = "Email", description = "이메일 관련 API")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "[A] 메일 전송", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PostMapping(path ="", consumes = "multipart/form-data")
    public ResponseModel broadcastEmail(
            EmailDto emailDto,
            @RequestPart(required = false) List<MultipartFile> files
    ) throws PermissionDeniedException {
        emailService.broadcastEmail(emailDto, files);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 전체 메일 전송", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PostMapping(path ="/all", consumes = "multipart/form-data")
    public ResponseModel broadcastEmailToAllMember(
            EmailDto emailDto,
            @RequestPart(required = false) List<MultipartFile> files
    ) throws PermissionDeniedException {
        emailService.broadcastEmailToAllMember(emailDto, files);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
