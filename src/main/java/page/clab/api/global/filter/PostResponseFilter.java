package page.clab.api.global.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * {@code PostResponseFilter}는 특정 API 요청에 대해 POST 요청의 응답 상태 코드를 수정하는 서블릿 필터입니다.
 *
 * <p>이 필터는 모든 API URL 패턴("/api/*")에 대해 등록되며, Spring 컴포넌트로 작동합니다.
 * 만약 POST 요청의 응답 상태 코드가 200(OK)일 경우, 이를 201(Created)로 변경하여 리소스 생성에 대한 명확한 상태 코드를 제공합니다.</p>
 */
@WebFilter("/api/*")
@Component
public class PostResponseFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // POST 요청이고 상태 코드가 200인 경우 상태 코드를 201로 변경
        if (HttpMethod.POST.name().equalsIgnoreCase(httpRequest.getMethod())
            && httpResponse.getStatus() == HttpServletResponse.SC_OK) {
            httpResponse.setStatus(HttpServletResponse.SC_CREATED);
        }

        chain.doFilter(request, response);
    }
}
