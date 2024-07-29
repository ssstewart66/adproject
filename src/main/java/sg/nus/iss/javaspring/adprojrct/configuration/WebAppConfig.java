package sg.nus.iss.javaspring.adprojrct.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sg.nus.iss.javaspring.adprojrct.Interceptor.AuthenticationInterceptor;

@Component
public class WebAppConfig implements WebMvcConfigurer {
    @Autowired
    AuthenticationInterceptor authenticationInterceptor;

/*    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/logout", "/register","/policy","/resources/**", "/static/**", "/error");
    }*/
}
