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
