package prosia.app.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Adi R.
 */
@Configuration
public class ContextInitializer {

    @Bean
    public ServletContextInitializer initializer() {
        return new ServletContextInitializer() {

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
                servletContext.setInitParameter("javax.faces.DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE", "true");
                servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "false");
                servletContext.setInitParameter("primefaces.THEME", "barcelona-cyan");
//                servletContext.setInitParameter("primefaces.THEME", "barcelona-dark-blue");
                servletContext.setInitParameter("primefaces.FONT_AWESOME", "true");
                servletContext.setInitParameter("primefaces.UPLOADER", "commons");
                servletContext.setInitParameter("org.apache.myfaces.ERROR_HANDLING", "false");
                servletContext.setInitParameter("facelets.DEVELOPMENT", "true");
            }
        };
    }

}
