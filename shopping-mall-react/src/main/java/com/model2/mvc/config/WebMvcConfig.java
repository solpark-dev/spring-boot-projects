package com.model2.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.model2.mvc.common.interceptor.AdminCheckInterceptor;
import com.model2.mvc.common.interceptor.LogonCheckInterceptor;

/**
 * Spring MVC 설정 클래스
 * 
 * 기존 common-servlet.xml의 내용을 Java Config로 변환
 * - 인터셉터 등록
 * - 정적 리소스 핸들링
 * - 파일 업로드 설정
 * - View Controller 등록 (Welcome Page)
 * 
 * @Configuration: 이 클래스가 설정 클래스임을 표시
 * WebMvcConfigurer: Spring MVC 설정을 커스터마이징하기 위한 인터페이스
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * View Controller 등록
     * 
     * 별도의 Controller 없이 URL을 View로 직접 매핑
     * 
     * 루트 경로("/")를 index.jsp로 매핑
     * - 사용자가 http://localhost:8080 접속 시
     * - /WEB-INF/views/index.jsp를 표시
     * 
     * @param registry View Controller 레지스트리
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        System.out.println("==> WebMvcConfig: View Controller 등록 시작");
        
        // 루트 경로를 index.jsp로 매핑
        registry.addViewController("/").setViewName("index");
        
        System.out.println("==> WebMvcConfig: View Controller 등록 완료");
    }

    /**
     * 인터셉터 등록
     * 
     * 기존 common-servlet.xml의 <mvc:interceptors> 내용을 Java로 변환
     * 
     * 인터셉터 순서:
     * 1. LogonCheckInterceptor: 로그인 필요한 페이지 접근 체크
     * 2. AdminCheckInterceptor: 관리자 권한 필요한 페이지 접근 체크
     * 
     * @param registry 인터셉터 레지스트리
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("==> WebMvcConfig: 인터셉터 등록 시작");
        
        // 1. 로그인 체크 인터셉터
        registry.addInterceptor(logonCheckInterceptor())
                .addPathPatterns(
                    "/user/updateUser*",           // 회원정보 수정
                    "/user/listUser*",             // 회원목록 (관리자도 체크 필요)
                    "/purchase/**",                // 모든 구매 관련
                    "/cart/**",                    // 장바구니
                    "/mypage/**"                   // 마이페이지
                )
                .excludePathPatterns(
                    "/user/login*",                // 로그인 페이지
                    "/user/addUser*",              // 회원가입
                    "/user/checkDuplication*"      // 중복체크
                );
        
        // 2. 관리자 체크 인터셉터
        registry.addInterceptor(adminCheckInterceptor())
                .addPathPatterns(
                    "/user/listUser*",             // 회원목록 조회
                    "/product/addProduct*",        // 상품 등록
                    "/product/updateProduct*",     // 상품 수정
                    "/category/**"                 // 카테고리 관리
                );
        
        System.out.println("==> WebMvcConfig: 인터셉터 등록 완료");
    }

    /**
     * 정적 리소스 핸들링 설정
     * 
     * /resources/** 요청을 /static/ 디렉토리로 매핑
     * 예: /resources/css/style.css → /static/css/style.css
     * 
     * @param registry 리소스 핸들러 레지스트리
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("==> WebMvcConfig: 정적 리소스 핸들러 등록");
        
        // 정적 리소스 경로 매핑
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600); // 캐시 시간: 1시간
        
        // 이미지 업로드 경로 매핑 (옵션)
        // 실제 파일 시스템 경로를 웹 경로로 노출
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/work/sol/images/")
                .setCachePeriod(3600);
    }

    /**
     * LogonCheckInterceptor Bean 등록
     * 
     * 기존: XML에서 <bean> 태그로 등록
     * 변경: @Bean 어노테이션으로 등록
     * 
     * @return LogonCheckInterceptor 인스턴스
     */
    @Bean
    public LogonCheckInterceptor logonCheckInterceptor() {
        System.out.println("==> WebMvcConfig: LogonCheckInterceptor Bean 생성");
        return new LogonCheckInterceptor();
    }

    /**
     * AdminCheckInterceptor Bean 등록
     * 
     * @return AdminCheckInterceptor 인스턴스
     */
    @Bean
    public AdminCheckInterceptor adminCheckInterceptor() {
        System.out.println("==> WebMvcConfig: AdminCheckInterceptor Bean 생성");
        return new AdminCheckInterceptor();
    }

    /**
     * 파일 업로드 Resolver 설정 (CommonsMultipartResolver)
     * 
     * 기존: web.xml의 <multipart-config>
     * 변경: @Bean으로 등록
     * 
     * 참고: Spring Boot는 기본적으로 StandardServletMultipartResolver를 사용하므로
     *      application.properties의 spring.servlet.multipart.* 설정으로 충분합니다.
     *      만약 CommonsMultipartResolver를 사용하고 싶다면 이 Bean을 활성화하세요.
     * 
     * @return CommonsMultipartResolver 인스턴스
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        System.out.println("==> WebMvcConfig: CommonsMultipartResolver Bean 생성");
        
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(20971520);        // 20MB
        resolver.setMaxInMemorySize(1048576);       // 1MB
        resolver.setDefaultEncoding("UTF-8");
        
        return resolver;
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)  // ⭐ 세션 쿠키 허용
                .maxAge(3600);
    }
}