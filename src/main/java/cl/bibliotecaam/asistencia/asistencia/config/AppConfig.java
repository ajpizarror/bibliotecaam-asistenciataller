package cl.bibliotecaam.asistencia.asistencia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${taller.url}")
    private String tallerUrl;
}
