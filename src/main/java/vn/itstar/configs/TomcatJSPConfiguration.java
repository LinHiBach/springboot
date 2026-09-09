package vn.itstar.configs;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatJSPConfiguration {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory>
            staticResourceCustomizer() {

        // Force JVM file.encoding = UTF-8 để Jasper đọc JSP đúng encoding
        System.setProperty("file.encoding", "UTF-8");

        return tomcatFactory -> {
            tomcatFactory.addContextCustomizers(context -> {
                // Đăng ký JSP resource configurer
                context.addLifecycleListener(new JSPStaticResourceConfigurer(context));

                // Cấu hình JspServlet encoding trực tiếp
                configureJspServletEncoding(context);
            });
        };
    }

    private void configureJspServletEncoding(Context context) {
        // Tìm JspServlet wrapper đã có trong context
        Wrapper jspServlet = (Wrapper) context.findChild("jsp");
        if (jspServlet != null) {
            jspServlet.addInitParameter("javaEncoding", "UTF-8");
            jspServlet.addInitParameter("jspEncoding", "UTF-8");
        }
    }
}