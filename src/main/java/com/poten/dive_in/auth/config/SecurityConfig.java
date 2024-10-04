package com.poten.dive_in.auth.config;

import com.poten.dive_in.auth.jwt.JwtAuthenticationFilter;
import com.poten.dive_in.auth.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtTokenProvider jwtTokenProvider(@Value("${jwt.secret_key}") String secretKey) {
        return new JwtTokenProvider(secretKey);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .formLogin(formLogin -> formLogin.disable())  // Form 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())  // HTTP 기본 인증 비활성화
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // /pools/** 경로에만 인증 필요, 나머지는 필터 적용하지 않음
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/pools").hasRole("USER")
                        .anyRequest().permitAll()  // 나머지는 모두 허용
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
