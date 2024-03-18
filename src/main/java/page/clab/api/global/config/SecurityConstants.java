package page.clab.api.global.config;

public class SecurityConstants {

    public static final String[] PERMIT_ALL = {
            "/login/**",
            "/static/**",
            "/resources/files/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/error",
            "/"
    };

    public static final String[] PERMIT_ALL_API_ENDPOINTS_GET = {
            "/applications/{studentId}",
            "/recruitments",
            "/news", "/news/**",
            "/blogs", "/blogs/**",
            "/executives", "/executives/**",
            "/awards", "/awards/**",
            "/activity-group", "/activity-group/**",
            "/work-experiences", "/work-experiences/**",
            "/products", "/products/**",
            "/reviews", "/reviews/**",
            "/activity-photos", "/activity-photos/**"
    };

    public static final String[] PERMIT_ALL_API_ENDPOINTS_POST = {
            "/applications",
            "/members/password-reset-requests",
            "/members/password-reset-verifications",
    };

}
