package page.clab.api.global.config;

public class SecurityConstants {

    public static final String[] PERMIT_ALL = {
            "/api/v1/login/**",
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

    public static final String[] PERMIT_ALL_API_ENDPOINTS_GET = {
            "/api/v1/applications/{studentId}",
            "/api/v1/recruitments",
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

    public static final String[] PERMIT_ALL_API_ENDPOINTS_POST = {
            "/api/v1/applications",
            "/api/v1/members/password-reset-requests",
            "/api/v1/members/password-reset-verifications",
    };
}
