package cl.bibliotecaam.asistencia.asistencia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfigTaller {
    @Value("${taller.url}")
    private String tallerUrl;

    @Bean
    public WebClient webClientTaller(){
        return  WebClient.builder()
                .baseUrl(tallerUrl)
                .build();
    }


}
