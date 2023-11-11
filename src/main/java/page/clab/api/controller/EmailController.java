package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.EmailService;
import page.clab.api.service.MemberService;
import page.clab.api.type.dto.EmailDto;
import page.clab.api.type.dto.ResponseModel;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
@Tag(name = "Email")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    private final MemberService memberService;

    @Operation(summary = "Send email", description = "Send email")
    @PostMapping("")
    public ResponseModel broadcastEmail(
            @RequestBody EmailDto emailDto
    ) throws MessagingException, PermissionDeniedException {
        emailService.broadcastEmail(emailDto);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "Broadcast email", description = "Broadcast email")
    @PostMapping("/broadcast")
    public ResponseModel broadcastEmailToAllMember(
            @RequestBody EmailDto emailDto
    ) throws PermissionDeniedException {
        emailService.broadcastEmailToAllMember(emailDto);
        return ResponseModel.builder().build();
    }

}
