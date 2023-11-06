package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.EmailService;
import page.clab.api.service.MemberService;
import page.clab.api.type.dto.MemberResponseDto;
import page.clab.api.type.dto.ResponseModel;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Tag(name = "Email")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    private final MemberService memberService;

    @Operation(summary = "Send email", description = "Send email")
    @PostMapping(path="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseModel sendEmail(@RequestParam String to,
                                   @RequestParam String subject,
                                   @RequestParam String content,
                                   @RequestParam(value = "multipartfile", required = false) MultipartFile file
    ) throws MessagingException, IOException {
        emailService.sendEmail(to, subject, content, file);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "Broadcast email", description = "Broadcast email")
    @PostMapping(path="/broadcast", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseModel broadcastEmailToAllMember(@RequestParam String subject,
                                                   @RequestParam String content,
                                                   @RequestParam(value = "multipartfile", required = false) MultipartFile file
    ) throws PermissionDeniedException, MessagingException, IOException {
        List<MemberResponseDto> memberList = memberService.getMembers();
        emailService.broadcastEmailToAllMember(memberList, subject, content, file);
        return ResponseModel.builder().build();
    }


}
