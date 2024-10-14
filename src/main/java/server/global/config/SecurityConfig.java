package server.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import server.domain.member.repository.MemberRepository;
import server.global.auth.oauth2.domain.AccessTokenAuthenticationProvider;
import server.global.auth.oauth2.filter.OAuth2AccessTokenAuthenticationFilter;
import server.global.auth.security.filter.CustomAccessDeniedHandler;
import server.global.auth.security.filter.CustomAuthenticationEntryPoint;
import server.global.auth.security.filter.JsonUsernamePasswordAuthenticationFilter;
import server.global.auth.security.filter.JwtAuthenticationFilter;
import server.global.auth.security.handler.LoginFailureHandler;
import server.global.auth.security.handler.LoginSuccessJWTProvideHandler;
import server.global.auth.security.service.JwtService;
import server.global.util.RedisUtil;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@Log4j
@ComponentScan(basePackages  = {"server.global.auth"})
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final AccessTokenAuthenticationProvider provider;
    private final OAuthProperties oAuthProperties;


    // 문자셋 필터
    public CharacterEncodingFilter encodingFilter() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return encodingFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(encodingFilter(), CsrfFilter.class)
                .formLogin().disable()//1 - formLogin 인증방법 비활성화
                .httpBasic().disable()//2 - httpBasic 인증방법 비활성화(특정 리소스에 접근할 때 username과 password 물어봄)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeRequests()
                .antMatchers("/api/member/signUp", "/api/member/login/oauth2", "/api/message/**", "/ws/**", "/socket.io/**").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**", "/order/room/**").permitAll()
                .anyRequest().authenticated();

        http
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler(jwtService))
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http
                .addFilterAfter(jsonUsernamePasswordLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(auth2AccessTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    OAuth2AccessTokenAuthenticationFilter auth2AccessTokenAuthenticationFilter() {
        return new OAuth2AccessTokenAuthenticationFilter(provider, loginSuccessJWTProvideHandler(), loginFailureHandler(), oAuthProperties);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {//2 - AuthenticationManager 등록
        DaoAuthenticationProvider provider = daoAuthenticationProvider();//DaoAuthenticationProvider 사용
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler(){
        return new LoginSuccessJWTProvideHandler(jwtService, memberRepository, redisUtil);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }



    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter() throws Exception {
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter = new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        jsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        jsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessJWTProvideHandler());
        jsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());

        jsonUsernamePasswordLoginFilter.setFilterProcessesUrl("/api/member/login");
        return jsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationProcessingFilter(){
        return new JwtAuthenticationFilter(jwtService, memberRepository, redisUtil);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174", "http://172.30.1.76:3000", "http://localhost:3000"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization", "Authorization-refresh"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(corsConfiguration.getMaxAge());

        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
        corsConfigSource.registerCorsConfiguration("/**", corsConfiguration);
        return corsConfigSource;
    }

//    @Bean
//    public LogoutSuccessHandler logoutSuccessHandler() {
//        return (request, response, authentication) -> {
//            if (response.getStatus() != 401) {
//                response.setContentType("application/json");
//                response.setCharacterEncoding("utf-8");
//                response.getWriter().write(objectMapper.writeValueAsString(
//                        ApiResponse.onSuccess(MemberResponseDto.MemberTaskSuccessResponseDto.builder().isSuccess(true).build())));
//            }
//        };
//    }

//    @Bean
//    public LogoutHandler logoutHandler() {
//        return new LogoutHandlerImpl(memberRepository, jwtService, redisUtil, objectMapper);
//    }



}
