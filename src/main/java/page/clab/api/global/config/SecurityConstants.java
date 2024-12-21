package page.clab.api.global.config;

public class SecurityConstants {

    protected static final String[] PERMIT_ALL = {
        "/static/**",
        "/actuator/health",
        "/resources/files/**",
        "/configuration/ui",
        "/configuration/security",
        "/favicon.ico",
        "/webjars/**",
        "/error",
        "/"
    };
    protected static final String[] PERMIT_ALL_API_ENDPOINTS_GET = {
        "/api/v1/applications/{recruitmentId}/{studentId}",
        "/api/v1/recruitments", "/api/v1/recruitments/recent-week",
        "/api/v1/news", "/api/v1/news/**",
        "/api/v1/blogs", "/api/v1/blogs/**",
        "/api/v1/positions", "/api/v1/positions/**",
        "/api/v1/awards", "/api/v1/awards/**",
        "/api/v1/activity-group", "/api/v1/activity-group/**",
        "/api/v1/work-experiences", "/api/v1/work-experiences/**",
        "/api/v1/products", "/api/v1/products/**",
        "/api/v1/reviews", "/api/v1/reviews/**",
        "/api/v1/activity-photos", "/api/v1/activity-photos/**"
    };
    protected static final String[] PERMIT_ALL_API_ENDPOINTS_POST = {
        "/api/v1/auth/login",
        "/api/v1/auth/login/guest",
        "/api/v1/auth/2fa",
        "/api/v1/applications",
        "/api/v1/members/password-reset-requests",
        "/api/v1/members/password-reset-verifications",
    };

    private SecurityConstants() {
    }
}
