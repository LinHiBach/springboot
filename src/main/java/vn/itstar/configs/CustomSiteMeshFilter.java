package vn.itstar.configs;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class CustomSiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {

        builder
            // SiteMesh 3.3 tự thêm tiền tố /WEB-INF/decorators/.
            .addDecoratorPath("/admin", "admin.jsp")
            .addDecoratorPath("/admin/*", "admin.jsp")
            .addDecoratorPath("/admin/**", "admin.jsp")
            .addDecoratorPath("/*", "web.jsp")

            .addExcludedPath("/login")
            .addExcludedPath("/login/*")
            .addExcludedPath("/register")
            .addExcludedPath("/register/*")
            .addExcludedPath("/verify-otp")
            .addExcludedPath("/verify-otp/*")
            .addExcludedPath("/resend-otp")
            .addExcludedPath("/resend-otp/*")
            .addExcludedPath("/alogin")
            .addExcludedPath("/alogin/*")
            .addExcludedPath("/api/*")
            .addExcludedPath("/static/*")
            .addExcludedPath("/error")
            .addExcludedPath("/WEB-INF/decorators/*");
    }
}
