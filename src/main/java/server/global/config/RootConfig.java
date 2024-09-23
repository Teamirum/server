package server.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import server.global.oauth.domain.authcode.AuthCodeRequestUrlProvider;
import server.global.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import server.global.oauth.infra.oauth.kakao.authcode.KakaoAuthCodeRequestUrlProvider;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

@Configuration
@PropertySource({"classpath:/application.properties"})
@MapperScan(basePackages  = {
        "server.domain.member.mapper",
        "server.global.security.mapper"
})
@ComponentScan(basePackages = {
        "server.domain",
        "server.global",
        "server.global.oauth.infra.oauth.kakao" // Kakao 관련 패키지 추가
})
@Slf4j
@EnableTransactionManagement
public class RootConfig {
    @Value("${jdbc.driver}") String driver;
    @Value("${jdbc.url}") String url;
    @Value("${jdbc.username}") String username;
    @Value("${jdbc.password}") String password;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private KakaoOauthConfig kakaoOauthConfig; // KakaoOauthConfig 주입

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setConfigLocation(
                applicationContext.getResource("classpath:/mybatis-config.xml"));
        sqlSessionFactory.setDataSource(dataSource());
        return sqlSessionFactory.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    // KakaoAuthCodeRequestUrlProvider 빈 등록
    @Bean
    public KakaoAuthCodeRequestUrlProvider kakaoAuthCodeRequestUrlProvider() {
        return new KakaoAuthCodeRequestUrlProvider(kakaoOauthConfig);
    }

    // AuthCodeRequestUrlProviderComposite 빈 등록
    @Bean
    public AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite() {
        Set<AuthCodeRequestUrlProvider> providers = new HashSet<>();
        providers.add(kakaoAuthCodeRequestUrlProvider());
        // 추가적인 AuthCodeRequestUrlProvider 구현체를 여기에 추가

        return new AuthCodeRequestUrlProviderComposite(providers);
    }
}