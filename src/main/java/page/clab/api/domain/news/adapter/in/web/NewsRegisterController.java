package page.clab.api.domain.news.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.news.application.port.in.RegisterNewsUseCase;
import page.clab.api.domain.news.application.dto.request.NewsRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스")
public class NewsRegisterController {

    private final RegisterNewsUseCase registerNewsUseCase;

    @Operation(summary = "뉴스 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({ "ROLE_ADMIN", "ROLE_SUPER" })
    @PostMapping("")
    public ApiResponse<Long> registerNews(
            @Valid @RequestBody NewsRequestDto requestDto
    ) {
        Long id = registerNewsUseCase.registerNews(requestDto);
        return ApiResponse.success(id);
    }
}

