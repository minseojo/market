package demo.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080") // 허용할 도메인(프론트엔드 도메인)을 설정합니다.
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드를 설정합니다.
                .allowedHeaders("*") // 요청 헤더를 설정합니다. 모든 헤더를 허용할 경우 "*"
                .allowCredentials(true); // 자격 증명 허용 여부를 설정합니다. (ex: 쿠키 허용)
    }
}
