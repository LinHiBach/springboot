package vn.itstar.configs;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class SiteMeshConfiguration {

    @Bean
    public FilterRegistrationBean<CustomSiteMeshFilter> siteMeshFilter() {

        FilterRegistrationBean<CustomSiteMeshFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(new CustomSiteMeshFilter());
        registration.setName("customSiteMeshFilter");

        // Áp dụng cho mọi request
        registration.addUrlPatterns("/*");

        // Chạy sau filter UTF-8 đã tạo
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);

        return registration;
    }
}