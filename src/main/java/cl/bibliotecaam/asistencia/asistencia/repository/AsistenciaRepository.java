package cl.bibliotecaam.asistencia.asistencia.repository;

import cl.bibliotecaam.asistencia.asistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByIdUsuario(Long id);
    List<Asistencia> findByIdTaller(Long id);
}
