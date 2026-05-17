package cl.bibliotecaam.asistencia.asistencia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    @Value("${taller.url}")
    private String tallerUrl;

    @Value("${usuario.url}")
    private String usuarioUrl;

    @Bean
    public WebClient webClientTaller(){
        return  WebClient.builder()
                .baseUrl(tallerUrl)
                .build();
    }

    @Bean
    public WebClient webClientUsuario(){
        return WebClient.builder()
                .baseUrl(usuarioUrl)
                .build();
    }
}
