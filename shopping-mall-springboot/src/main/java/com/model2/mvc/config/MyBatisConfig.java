package com.model2.mvc.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis 설정 클래스
 * 
 * 기존 context-mybatis.xml의 내용을 Java Config로 변환
 * - SqlSessionFactory 설정
 * - Mapper 스캔
 * - 트랜잭션 관리자 설정
 * 
 * @Configuration: 설정 클래스 표시
 * @EnableTransactionManagement: 트랜잭션 관리 활성화 (기존 context-transaction.xml)
 * @MapperScan: Mapper 인터페이스 자동 스캔
 * 
 * 참고: Spring Boot의 MyBatis Auto Configuration이 대부분을 자동 설정하므로,
 *      이 클래스는 커스터마이징이 필요한 경우에만 사용합니다.
 *      기본 설정으로 충분하다면 이 클래스 전체를 주석처리 가능합니다.
 */
@Configuration
@EnableTransactionManagement  // 트랜잭션 관리 활성화
@MapperScan(
    basePackages = "com.model2.mvc.service.*.dao",  // Mapper 인터페이스 스캔 경로
    sqlSessionFactoryRef = "sqlSessionFactory"       // 사용할 SqlSessionFactory
)
public class MyBatisConfig {

    @Value("${mybatis.config-location}")
    private String mybatisConfigLocation;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    /**
     * SqlSessionFactory Bean 생성
     * 
     * MyBatis의 핵심 객체로, SQL 세션을 생성하는 팩토리
     * 
     * 기존: context-mybatis.xml의 <bean id="sqlSessionFactory">
     * 변경: @Bean 메소드로 생성
     * 
     * @param dataSource 데이터소스 (자동 주입)
     * @param applicationContext 스프링 컨텍스트 (자동 주입)
     * @return SqlSessionFactory 인스턴스
     * @throws Exception 설정 오류 발생시
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(
            DataSource dataSource, 
            ApplicationContext applicationContext) throws Exception {
        
        System.out.println("==> MyBatisConfig: SqlSessionFactory 생성 시작");
        
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        
        // 데이터소스 설정
        sessionFactory.setDataSource(dataSource);
        
        // MyBatis 설정 파일 위치 지정
        Resource configLocation = applicationContext.getResource(mybatisConfigLocation);
        sessionFactory.setConfigLocation(configLocation);
        
        // Mapper XML 파일 위치 지정
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperResources = resolver.getResources(mapperLocations);
        sessionFactory.setMapperLocations(mapperResources);
        
        System.out.println("==> MyBatis Config Location: " + mybatisConfigLocation);
        System.out.println("==> MyBatis Mapper Count: " + mapperResources.length);
        
        return sessionFactory.getObject();
    }

    /**
     * 트랜잭션 관리자 Bean 생성
     * 
     * 기존: context-transaction.xml의 <bean id="transactionManager">
     * 변경: @Bean 메소드로 생성
     * 
     * @param dataSource 데이터소스 (자동 주입)
     * @return PlatformTransactionManager 인스턴스
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        System.out.println("==> MyBatisConfig: TransactionManager 생성");
        return new DataSourceTransactionManager(dataSource);
    }
}
