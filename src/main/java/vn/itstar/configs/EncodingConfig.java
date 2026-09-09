package vn.itstar.configs;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class EncodingConfig {

    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter>
            utf8FilterRegistration() {

        CharacterEncodingFilter filter =
                new CharacterEncodingFilter();

        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        FilterRegistrationBean<CharacterEncodingFilter> registration =
                new FilterRegistrationBean<>(filter);

        registration.setName("utf8EncodingFilter");
        registration.addUrlPatterns("/*");

        // Chạy trước SiteMesh
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return registration;
    }
}