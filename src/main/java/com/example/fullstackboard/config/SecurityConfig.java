package com.example.fullstackboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // REST API 기본 세팅
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 접속 허용 범위
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/health",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/h2-console/**"
                        ).permitAll()
                        // 지금은 개발 단계라 전부 열어두고,
                        // 추후 JWT 도입하면 anyRequest().authenticated()로 바꿈
                        .anyRequest().permitAll()
                )

                // H2 콘솔이 frame을 쓰므로 sameOrigin 허용
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // 폼 로그인/HTTP Basic 비활성화 (로그인 페이지 없앰)
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
