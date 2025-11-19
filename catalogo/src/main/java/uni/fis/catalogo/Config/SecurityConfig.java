package uni.fis.catalogo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import uni.fis.catalogo.Security.CatalogoOwnershipFilter;
import uni.fis.catalogo.Security.JwtAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CatalogoOwnershipFilter catalogoOwnershipFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                
                .requestMatchers("POST", "/api/catalogo/crear")
                    .hasRole("PROVEEDOR")
                
                .requestMatchers("POST", "/api/catalogo/{catalogoId}/producto")
                    .hasRole("PROVEEDOR")
                    
                .requestMatchers("POST", "/api/catalogo/{catalogoId}/servicio")
                    .hasRole("PROVEEDOR")
                    
                .requestMatchers("GET", "/{idCatalogo}/producto/{id}")
                    .hasRole("PROVEEDOR")
                    
                .requestMatchers("GET", "/{idCatalogo}/servicio/{id}")
                    .hasRole("PROVEEDOR")
                    
                .requestMatchers("DELETE", "/{idCatalogo}/producto/{id}/eliminar")
                    .hasRole("PROVEEDOR")
                
                .requestMatchers("DELETE", "/{idCatalogo}/servicio/{id}/eliminar")
                    .hasRole("PROVEEDOR")
                    
                .requestMatchers("DELETE", "/api/catalogo/{id}")
                    .hasRole("PROVEEDOR")

                .requestMatchers("GET", "/api/catalogo/{catalogoId}/listaProductos")
                    .permitAll()
                .requestMatchers("GET", "/api/catalogo/{catalogoId}/listaServicios")
                    .permitAll()
                .requestMatchers("POST", "/api/catalogo/producto/{id}/calificar")
                    .permitAll()
                .requestMatchers("POST", "/api/catalogo/servicio/{id}/calificar")
                    .permitAll()
                .anyRequest()
                    .hasRole("PROVEEDOR")
            )
            
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(catalogoOwnershipFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}