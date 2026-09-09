package vn.itstar.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
public class JspViewConfiguration {

    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver =
                new InternalResourceViewResolver();

        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");

        resolver.setContentType("text/html;charset=UTF-8");

        // Cần khi dùng SiteMesh Filter với JSP trên Tomcat 11
        resolver.setAlwaysInclude(true);

        resolver.setOrder(0);

        return resolver;
    }
}