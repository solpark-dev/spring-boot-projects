package com.model2.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Shopping Mall Spring Boot 애플리케이션 메인 클래스
 * 
 * @SpringBootApplication 어노테이션은 다음 3가지를 포함합니다:
 * 1. @Configuration: 이 클래스가 설정 클래스임을 표시
 * 2. @EnableAutoConfiguration: Spring Boot의 자동 설정 활성화
 * 3. @ComponentScan: 이 패키지와 하위 패키지의 컴포넌트를 스캔
 * 
 * SpringBootServletInitializer 상속:
 * - 외부 Tomcat에 WAR 파일로 배포할 때 필요
 * - JSP 사용시 필수
 */
@SpringBootApplication
public class ShoppingMallApplication extends SpringBootServletInitializer {

    /**
     * 메인 메소드 - 애플리케이션 시작점
     * 
     * 실행 방법:
     * 1. IDE에서 직접 실행
     * 2. mvn spring-boot:run
     * 3. java -jar target/shopping-mall.war
     * 
     * @param args 커맨드 라인 인자
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Shopping Mall Application Starting... ");
        System.out.println("========================================");
        
        SpringApplication.run(ShoppingMallApplication.class, args);
        
        System.out.println("========================================");
        System.out.println(" Application Started Successfully!     ");
        System.out.println(" Access: http://localhost:8080         ");
        System.out.println("========================================");
    }

    /**
     * 외부 Tomcat에 배포시 사용되는 설정
     * WAR 파일로 배포할 때 필요
     * 
     * @param builder SpringApplicationBuilder
     * @return 설정된 SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShoppingMallApplication.class);
    }
}
