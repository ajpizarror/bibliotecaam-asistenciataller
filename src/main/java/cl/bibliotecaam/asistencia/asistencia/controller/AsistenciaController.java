package cl.bibliotecaam.asistencia.asistencia.controller;

import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaRequestDTO;
import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaResponseDTO;
import cl.bibliotecaam.asistencia.asistencia.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bibliotecaam/asistencia")
@RequiredArgsConstructor
public class AsistenciaController {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> eliminar (@PathVariable Long id){
        if (asistenciaService.obtenerPorId(id).isEmpty()){
            Map<String, String> borrado = new LinkedHashMap<>();
            borrado.put("¡ERROR! ", "¡La asistencia con id "+id+" no fue encontrada!");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(borrado);
        }else {
            asistenciaService.eliminarPorId(id);
            Map<String, String> borrado = new LinkedHashMap<>();
            borrado.put("¡EXITO! ", "¡La asistencia fue eliminada con exito!");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(borrado);
        }
    }
}
