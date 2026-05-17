package cl.bibliotecaam.asistencia.asistencia.service;

import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaRequestDTO;
import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaResponseDTO;
import cl.bibliotecaam.asistencia.asistencia.model.Asistencia;
import cl.bibliotecaam.asistencia.asistencia.repository.AsistenciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsistenciaService {
    private final AsistenciaRepository asistenciaRepository;

    private final WebClient webClient;

    private AsistenciaResponseDTO mapToDTO(Asistencia asistencia){
        return new AsistenciaResponseDTO(
                asistencia.getIdAsistencia(),
                asistencia.getIdUsuario(),
                asistencia.getIdTaller()
        );
    }

    private void validarUsuario(Long idUsuario){
        try {
            webClient.get()
                    .uri("/api/bibliotecaam/usuario/{id}", idUsuario)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info(">>> Usuario {} validado correctamente (WebClient)", idUsuario);
        } catch (WebClientResponseException.NotFound e){
            throw new RuntimeException(
                    "El usuario con id" + idUsuario + "no se encontro en la base de datos de Usuario; Revise su ID");
        } catch (Exception e){
            throw new RuntimeException("ERROR - No se puede conectar con usuario: "+e.getMessage());
        }
    }

    private void validarTaller(Long idTaller){
        try {
            webClient.get()
                    .uri("/api/bibliotecaam/taller/{id}", idTaller)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info(">>> Taller {} validado correctamente (WebClient)", idTaller);
        } catch (WebClientResponseException.NotFound e){
            throw new RuntimeException(
                    "El taller con id" + idTaller + "no se encontro en la base de datos de Taller; Revise su ID");
        } catch (Exception e){
            throw new RuntimeException("ERROR - No se puede conectar con Taller: "+e.getMessage());
        }
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

    public AsistenciaResponseDTO guardar(AsistenciaRequestDTO dto){
        validarUsuario(dto.getIdUsuario());
        validarTaller(dto.getIdTaller());

        Asistencia asistencia = new Asistencia(
                null,
                dto.getIdTaller(),
                dto.getIdUsuario()
        );
        return mapToDTO(asistenciaRepository.save(asistencia));
    }

    public void eliminarPorId(Long id){
        asistenciaRepository.deleteById(id);
    }

    public Optional<AsistenciaResponseDTO> actualizar(Long id, AsistenciaRequestDTO doto){
        return asistenciaRepository.findById(id).map(existente -> {
            validarTaller(doto.getIdTaller());
            validarUsuario(doto.getIdUsuario());
            existente.setIdUsuario(doto.getIdUsuario());
            existente.setIdTaller(doto.getIdTaller());

            return mapToDTO(existente);
        });
    }

}
