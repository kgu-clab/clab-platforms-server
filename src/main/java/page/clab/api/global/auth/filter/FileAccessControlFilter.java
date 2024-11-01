package page.clab.api.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.util.ResponseUtil;

/**
 * {@code FileAccessControlFilter}는 파일 접근 요청에 대한 접근 제어를 수행하는 필터입니다.
 *
 * <p>특정 URL로 시작하는 요청에 대해 사용자의 인증 정보를 검증하여, 접근 권한이 있는지 확인합니다.
 * 파일에 대한 접근 권한이 없는 경우 401 Unauthorized 응답을 반환하며, 권한이 있는 경우 요청을
 * 필터 체인에서 계속 처리합니다.</p>
 *
 * <p>이 필터는 주로 파일 다운로드 또는 이미지 접근 등 특정 파일 URL에 대한 접근 제어가 필요한
 * 상황에서 사용됩니다.</p>
 *
 * @see OncePerRequestFilter
 */
@RequiredArgsConstructor
public class FileAccessControlFilter extends OncePerRequestFilter {

    private final FileService fileService;
    private final String fileURL;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith(fileURL)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!fileService.isUserAccessibleAtFile(authentication, requestURI)) {
                ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
