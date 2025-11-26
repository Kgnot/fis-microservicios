package uni.fis.email.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        // FilterChain para endpoints de email con Basic Auth
        @Bean
        @Order(1)
        public SecurityFilterChain emailSecurityFilterChain(HttpSecurity http) throws Exception {
                http
                                .securityMatcher("/api/email/**") // ← Solo endpoints de email
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .anyRequest().authenticated() // Todos los /api/email/** requieren auth
                                )
                                .httpBasic(Customizer.withDefaults()); // Basic Auth

                return http.build();
        }

        // FilterChain para actuator (público)
        @Bean
        @Order(2)
        public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .securityMatcher("/actuator/**") // ← Solo actuator
                                .authorizeHttpRequests(auth -> auth
                                                .anyRequest().permitAll() // Público
                                )
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
                UserDetails admin = User.builder()
                                .username("admin")
                                .password("{noop}admin1234")
                                .roles("ADMIN")
                                .build();
                return new InMemoryUserDetailsManager(admin);
        }
}