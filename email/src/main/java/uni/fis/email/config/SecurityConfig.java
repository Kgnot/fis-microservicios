package uni.fis.email.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import uni.fis.email.security.JwtAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                
                .requestMatchers("POST", "/api/email/send")
                    .hasRole("ADMIN")
                
                .requestMatchers("GET", "/api/email/logs")
                    .hasRole("ADMIN")
                
                .requestMatchers("GET", "/api/email/logs/recipient/**")
                    .hasRole("ADMIN")
                
                .requestMatchers("GET", "/api/email/logs/user/**")
                    .hasRole("ADMIN")
                
                .requestMatchers("GET", "/api/email/logs/status/**")
                    .hasRole("ADMIN")
                
                .requestMatchers("GET", "/api/email/health")
                    .permitAll()
                
                .anyRequest()
                    .hasRole("ADMIN")
            )
            
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
