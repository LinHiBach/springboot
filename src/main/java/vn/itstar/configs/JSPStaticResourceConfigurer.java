package vn.itstar.configs;

import java.net.URI;
import java.net.URL;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.WebResourceRoot;
import org.springframework.util.ResourceUtils;

public class JSPStaticResourceConfigurer implements LifecycleListener {

    private final Context context;

    private final String subPath = "/META-INF";

    public JSPStaticResourceConfigurer(Context context) {
        this.context = context;
    }

    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        if (!Lifecycle.CONFIGURE_START_EVENT.equals(event.getType())) {
            return;
        }

        URL finalLocation = getUrl();

        context.getResources().createWebResourceSet(
                WebResourceRoot.ResourceSetType.RESOURCE_JAR,
                "/",
                finalLocation,
                subPath
        );
    }

    private URL getUrl() {
        URL location = getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation();

        if (ResourceUtils.isFileURL(location)) {
            return location;
        }

        if (ResourceUtils.isJarURL(location)
                || location.toString().startsWith("nested:")) {
            try {
                String locationStr = location.getPath()
                        .replaceFirst("^nested:", "")
                        .replaceFirst("/!BOOT-INF/classes/!/$", "!/");

                return URI.create("jar:file:" + locationStr).toURL();
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Không thể thêm nguồn JSP vào Tomcat", e
                );
            }
        }

        throw new IllegalStateException(
                "Không thể xử lý URL: " + location
        );
    }
}