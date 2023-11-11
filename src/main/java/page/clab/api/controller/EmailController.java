package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.EmailService;
import page.clab.api.type.dto.EmailDto;
import page.clab.api.type.dto.ResponseModel;

import java.util.List;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
@Tag(name = "Email")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "Send email", description = "Send email")
    @PostMapping(path ="")
    public ResponseModel broadcastEmail(
            @RequestBody EmailDto emailDto,
            @RequestPart List<MultipartFile> files
    ) throws PermissionDeniedException {
        emailService.broadcastEmail(emailDto, files);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "Broadcast email", description = "Broadcast email")
    @PostMapping(path ="/all")
    public ResponseModel broadcastEmailToAllMember(
            @RequestBody EmailDto emailDto,
            @RequestPart List<MultipartFile> files
    ) throws PermissionDeniedException {
        emailService.broadcastEmailToAllMember(emailDto, files);
        return ResponseModel.builder().build();
    }

}
