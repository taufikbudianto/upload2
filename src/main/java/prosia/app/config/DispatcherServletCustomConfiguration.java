package prosia.app.config;

import javax.servlet.MultipartConfigElement;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author Tommy
 */
@Configuration
public class DispatcherServletCustomConfiguration {

    private final String TMP_FOLDER = "C:\\Basarnas\\mobile\\tmp";
    private final int MAX_UPLOAD_SIZE = 5 * 1024 * 1024;

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public ServletRegistrationBean dispatcherServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                dispatcherServlet(), "/sarcore/*");
        registration.setName(
                DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
        registration.setLoadOnStartup(1);
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(TMP_FOLDER,
                MAX_UPLOAD_SIZE, MAX_UPLOAD_SIZE * 2, MAX_UPLOAD_SIZE / 2);

        registration.setMultipartConfig(multipartConfigElement);
        return registration;
    }

    @Bean
    public ServletRegistrationBean images() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                dispatcherServlet(), "/document/*");
        registration.setName(
                DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
        registration.setLoadOnStartup(1);
        return registration;
    }

}
