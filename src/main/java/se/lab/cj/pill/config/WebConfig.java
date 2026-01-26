package se.lab.cj.pill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * CORS 설정 모든 출처 허용 강제
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정
                .allowedOrigins("*") // 모든 출처의 요청을 허용
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용되는 HTTP 메소드 지정
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(false); // 인증된 요청을 허용, 로그인 정보가 필요없으므로 false
    }

}
