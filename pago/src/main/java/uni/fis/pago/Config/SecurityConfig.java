package uni.fis.pago.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import uni.fis.pago.Security.JwtAuthenticationFilter;
import uni.fis.pago.Security.PagoOwnershipFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PagoOwnershipFilter pagoOwnershipFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
           
                .requestMatchers("POST", "/api/pago/crearOrdenCompra").authenticated()
                
                .requestMatchers("POST", "/api/pago/crearPago/**").authenticated()
                
                .requestMatchers("GET", "/api/pago/ObtenerPago/{id}").authenticated()
                
                .requestMatchers("GET", "/api/pago/ObtenerOrdenCompra/{id}").authenticated()
                .requestMatchers("DELETE", "/api/pago/EliminarOrdenCompra/{idOrdenCompra}").authenticated()
                
                .requestMatchers("POST", "/api/pago/crearOrdenItem").authenticated()
                .requestMatchers("GET", "/api/pago/ObtenerOrdenItem/{idOrdenItem}").authenticated()
                .requestMatchers("GET", "/api/pago/ObtenerOrdenCompra/{idOrdenCompra}/OrdenesItems").authenticated()
                .requestMatchers("DELETE", "/api/pago/EliminarOrdenItem/{idOrdenItem}").authenticated()
                
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(pagoOwnershipFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}