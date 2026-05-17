package cl.bibliotecaam.asistencia.asistencia.config;

import cl.bibliotecaam.asistencia.asistencia.model.Asistencia;
import cl.bibliotecaam.asistencia.asistencia.repository.AsistenciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AsistenciaRepository asistenciaRepository;

    @Override
    public void run(String... args){
        if (asistenciaRepository.count()>0){
            log.info(">>> Data Initializer: La BD ya tiene datos, se omite la carga inicial.");
            return;
        }

        log.info(">>> Data Initializer: BD vacia detectada. Insertando datos de prueba.");

        asistenciaRepository.save(new Asistencia(null, 1L, 1L));

        asistenciaRepository.save(new Asistencia(
                null, 2L, 1L
        ));

        asistenciaRepository.save(new Asistencia(
                null, 1L, 2L
        ));
    }
}
