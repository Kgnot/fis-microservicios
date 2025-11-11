package FIS.proyectoFinal.ProveedorService.Infraestructura;

import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public KafkaAutoConfiguration kafkaAutoConfiguration(){
        return null;
    }
}
