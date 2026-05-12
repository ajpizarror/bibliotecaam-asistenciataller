package cl.bibliotecaam.asistencia.asistencia.service;

import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaRequestDTO;
import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaResponseDTO;
import cl.bibliotecaam.asistencia.asistencia.model.Asistencia;
import cl.bibliotecaam.asistencia.asistencia.repository.AsistenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaService {
    private final AsistenciaRepository asistenciaRepository;

    private AsistenciaResponseDTO mapToDTO(Asistencia asistencia){
        return new AsistenciaResponseDTO(
                asistencia.getIdAsistencia(),
                asistencia.getIdUsuario(),
                asistencia.getIdTaller()
        );
    }

    public Optional<AsistenciaResponseDTO> obtenerPorId(Long id){
        return asistenciaRepository.findById(id).map(this::mapToDTO);
    }

    public List<AsistenciaResponseDTO> listarTodas(){
        return asistenciaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<AsistenciaResponseDTO> listarPorUsuario(Long id){
        return asistenciaRepository.findByIdUsuario(id)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<AsistenciaResponseDTO> listarPorTaller(Long id){
        return asistenciaRepository.findByIdTaller(id)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Asistencia guardar(Asistencia asistencia){
        return asistenciaRepository.save(asistencia);
    }

    public void eliminarPorId(Long id){
        asistenciaRepository.deleteById(id);
    }

    public Optional<AsistenciaResponseDTO> actualizar(Long id, AsistenciaRequestDTO doto){
        return asistenciaRepository.findById(id).map(existente -> {
            existente.setIdUsuario(doto.getIdUsuario());
            existente.setIdTaller(doto.getIdTaller());

            return mapToDTO(existente);
        });
    }

}
