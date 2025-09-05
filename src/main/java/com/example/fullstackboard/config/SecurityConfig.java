package com.example.fullstackboard.config;

import com.example.fullstackboard.config.jwt.JwtAuthenticationFilter;
import com.example.fullstackboard.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 기본 strength=10
    }

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
                                "/auth/login",
                                "/health",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/h2-console/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "members").permitAll()  // 회원가입 허용
                        .requestMatchers("/members/**").hasRole("ADMIN")
                        .requestMatchers("/posts/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // H2 콘솔이 frame을 쓰므로 sameOrigin 허용
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

        // 폼 로그인/HTTP Basic 비활성화 (로그인 페이지 없앰)
        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
