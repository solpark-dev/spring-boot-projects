package com.model2.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * 애플리케이션 공통 설정 클래스
 * 
 * 기존 context-common.xml의 내용을 Java Config로 변환
 * - Properties 파일 로드
 * - 공통 Bean 등록
 * - 소셜 로그인 서비스 Bean 등록
 * 
 * @Configuration: 설정 클래스 표시
 * @PropertySource: Properties 파일 위치 지정
 */
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    /**
     * PropertySourcesPlaceholderConfigurer Bean 생성
     * 
     * @Value 어노테이션으로 properties 값을 주입받기 위해 필요
     * 
     * 기존: context-common.xml의 <util:properties>
     * 변경: PropertySourcesPlaceholderConfigurer
     * 
     * @return PropertySourcesPlaceholderConfigurer 인스턴스
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        System.out.println("==> AppConfig: PropertySourcesPlaceholderConfigurer 생성");
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * 소셜 로그인 서비스들은 각 구현 클래스에 @Service 어노테이션으로 등록됨
     * 
     * - KakaoServiceImpl: @Service("kakaoServiceImpl")
     * - NaverServiceImpl: @Service("naverServiceImpl")
     * - GoogleServiceImpl: @Service("googleServiceImpl")
     * 
     * 따라서 여기서 중복 등록할 필요 없음
     */
}