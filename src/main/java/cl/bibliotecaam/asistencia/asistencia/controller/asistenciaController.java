package cl.bibliotecaam.asistencia.asistencia.controller;

import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaRequestDTO;
import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaResponseDTO;
import cl.bibliotecaam.asistencia.asistencia.model.Asistencia;
import cl.bibliotecaam.asistencia.asistencia.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bibliotecaam/asistencia")
@RequiredArgsConstructor
public class asistenciaController {
    private final AsistenciaService asistenciaService;

    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerTodas(){
        return ResponseEntity.ok(asistenciaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<AsistenciaResponseDTO>> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(asistenciaService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerPorUsuario(@PathVariable Long id){
        return ResponseEntity.ok(asistenciaService.listarPorUsuario(id));
    }

    @GetMapping("/taller/{id}")
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerPorTaller(@PathVariable Long id){
        return ResponseEntity.ok(asistenciaService.listarPorTaller(id));
    }

    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> guardar(@Valid @RequestBody AsistenciaRequestDTO doto){
        return ResponseEntity.status(201).body(asistenciaService.guardar(doto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AsistenciaRequestDTO doto){
        return asistenciaService.actualizar(id, doto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminar (@PathVariable Long id){
        if (asistenciaService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        asistenciaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
