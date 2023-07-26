package greeny.backend.config.security;

import greeny.backend.config.jwt.JwtAccessDeniedHandler;
import greeny.backend.config.jwt.JwtAuthenticationEntryPoint;
import greeny.backend.config.jwt.JwtFilter;
import greeny.backend.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private static final String[] AUTH_WHITELIST_WITH_MEMBER_AUTH = {
            "/swagger-ui/**",
            "/api-docs/**",
            "/api",
            "/api/auth",
            "/api/auth/sign-up/**",
            "/api/auth/sign-in/**",
            "/api/auth/password",
            "/api/auth/reissue"
    };

    private static final String[] AUTH_WHITELIST_WITH_GET_METHOD = {
            "/api/stores/**",
            "/api/posts/**",
            "/api/comments/**"
    };

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests(authorize -> authorize
                        .antMatchers(AUTH_WHITELIST_WITH_MEMBER_AUTH)
                        .permitAll()
                        .antMatchers(HttpMethod.GET, AUTH_WHITELIST_WITH_GET_METHOD)  // 인증 없이 조회 가능한 API 목록
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )

                .build();
    }
}