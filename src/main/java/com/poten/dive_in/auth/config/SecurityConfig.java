package com.poten.dive_in.auth.config;

import com.poten.dive_in.auth.jwt.JwtAuthenticationFilter;
import com.poten.dive_in.auth.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화
                .formLogin(AbstractHttpConfigurer::disable)  // Form 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)  // HTTP 기본 인증 비활성화
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                .requestMatchers(HttpMethod.GET, "/community/posts/**").permitAll()  // 모든 GET 요청에 대해 허용
                                .requestMatchers(HttpMethod.GET, "/community/comments/**").permitAll()  // 모든 GET 요청에 대해 허용
                                .requestMatchers(HttpMethod.GET, "/login/kakao").permitAll()
                                .requestMatchers(HttpMethod.GET, "/lessons/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/openGraph/fetch").permitAll()
                                .requestMatchers(HttpMethod.GET, "/pools/**").permitAll()
                                .requestMatchers("/user/profile", "/community/posts/user/**", "/community/comments/user/**").hasAnyRole("USER", "ADMIN")  // 로그인 후 접근 가능한 URL
//                                .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 기본 사용자 비활성화
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }

}
