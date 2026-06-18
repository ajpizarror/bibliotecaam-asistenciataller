package cl.bibliotecaam.asistencia.asistencia.config;

import cl.bibliotecaam.asistencia.asistencia.model.Asistencia;
import cl.bibliotecaam.asistencia.asistencia.repository.AsistenciaRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            Asistencia asistencia = new Asistencia();
            asistencia.setIdAsistencia((long) (i + 1));
            asistencia.setIdUsuario((long) (i + 1));
            asistencia.setIdTaller((long) (i + 1));
            asistenciaRepository.save(asistencia);
        }

    }
}
