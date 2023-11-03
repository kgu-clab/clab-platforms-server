package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.service.EmailService;
import page.clab.api.type.dto.EmailDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Tag(name = "Email")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "Send email")
    @PostMapping("/send")
    public ResponseModel sendEmail(@RequestBody EmailDto emailDto) {
        emailService.sendEmail(emailDto);
        return ResponseModel.builder().build();
    }

}
