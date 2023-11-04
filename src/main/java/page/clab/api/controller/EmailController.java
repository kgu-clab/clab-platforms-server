package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.EmailService;
import page.clab.api.service.MemberService;
import page.clab.api.type.dto.EmailDto;
import page.clab.api.type.dto.MemberResponseDto;
import page.clab.api.type.dto.ResponseModel;

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
    @PostMapping("/send")
    public ResponseModel sendEmail(@RequestParam String to,
                                   @RequestBody EmailDto emailDto) {
        emailService.sendEmail(to, emailDto);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "Broadcast email", description = "Broadcast email")
    @PostMapping("")
    public ResponseModel broadcastEmailToAllMember(@RequestBody EmailDto emailDto) throws PermissionDeniedException {
        List<MemberResponseDto> memberList = memberService.getMembers();
        emailService.broadcastEmailToAllMember(memberList, emailDto);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "Broadcast email to group", description = "Broadcast email to group")
    @PostMapping("/group")
    public ResponseModel broadcastEmailToGroup(@RequestParam String group,
                                               @RequestBody EmailDto emailDto) {

        return ResponseModel.builder().build();
    }

}
