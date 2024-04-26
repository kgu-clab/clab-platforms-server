package page.clab.api.global.common.email.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.email.application.EmailService;
import page.clab.api.global.common.email.dto.request.EmailDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
@Tag(name = "Email", description = "이메일")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "[A] 메일 전송", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<String>> broadcastEmail(
            EmailDto emailDto,
            @RequestParam(name = "multipartFile", required = false) List<MultipartFile> files
    ) {
        CompletableFuture<List<String>> emailTask = CompletableFuture.supplyAsync(() -> {
            return emailService.broadcastEmail(emailDto, files);
        });

        List<String> successfulAddresses = emailTask.join();
        return ApiResponse.success(successfulAddresses);
    }

    @Operation(summary = "[A] 전체 메일 전송", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(path = "/all", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<String>> broadcastEmailToAllMember(
            EmailDto emailDto,
            @RequestParam(name = "multipartFile", required = false) List<MultipartFile> files
    ) {
        CompletableFuture<List<String>> emailTask = CompletableFuture.supplyAsync(() -> {
            return emailService.broadcastEmailToAllMember(emailDto, files);
        });

        List<String> successfulEmails = emailTask.join();
        return ApiResponse.success(successfulEmails);
    }

}
