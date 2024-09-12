package server.global.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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

import javax.sql.DataSource;

//@Configuration : 설정,
@Configuration
// 어떤 경로에서 property를 가져올지 설정 (classpath를 붙여줘야 프로젝트의 루트로 접근)
// 그냥 /를 사용하면 webapp 폴더가 루트가 된다.
@PropertySource("classpath:/application.properties")
@MapperScan(basePackages = {"server.domain.member.mapper"}) // mapper의 위치 알려주기
@ComponentScan(basePackages = {"server.domain.member.service"})
@EnableTransactionManagement // 트랜잭션 관리 설정
public class RootConfig {
    // application.properties 파일에서 값을 읽어와서 변수에 주입
    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Autowired  // 스프링 컨텍스트에서 ApplicationContext 객체를 자동으로 주입
    ApplicationContext applicationContext;

    // MyBatis의 SqlSessionFactory를 빈으로 등록하는 메서드
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();  // SqlSessionFactoryBean 객체 생성
        sqlSessionFactory.setConfigLocation(
                applicationContext.getResource("classpath:/mybatis-config.xml"));  // MyBatis 설정 파일 위치 설정
        sqlSessionFactory.setDataSource(dataSource());  // DataSource 설정

        return sqlSessionFactory.getObject();  // SqlSessionFactory 객체를 반환
    }

    // 스프링의 DataSourceTransactionManager를 빈으로 등록하는 메서드
    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource());  // DataSourceTransactionManager 객체 생성
        return manager;  // DataSourceTransactionManager 객체를 반환
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

//        설정 객체에 데이터베이스 연결 정보를 설정
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

//        HikariDataSource 객체 생성 후 설정을 적용
        HikariDataSource dataSource = new HikariDataSource(config);

        return dataSource;
    }
}
