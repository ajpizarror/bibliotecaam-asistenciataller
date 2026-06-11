package cl.bibliotecaam.asistencia.asistencia.controller;

import cl.bibliotecaam.asistencia.asistencia.assemblers.AsistenciaModelAssembler;
import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaRequestDTO;
import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaResponseDTO;
import cl.bibliotecaam.asistencia.asistencia.model.Asistencia;
import cl.bibliotecaam.asistencia.asistencia.service.AsistenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping("/api/bibliotecaam/asistencia")
@RequiredArgsConstructor
@Tag(name = "Asistencias", description = "Operaciones asociadas a asistencias.")
public class AsistenciaController {
    private final AsistenciaService asistenciaService;

    @Autowired
    private AsistenciaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas las asistencias", description = "Obtiene una lista de todas las asistencias-")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
            @ApiResponse(responseCode = "404", description = "Asistencia no encontrada")
    })
    public ResponseEntity<CollectionModel<EntityModel<AsistenciaResponseDTO>>> obtenerTodas(){
        List<EntityModel<AsistenciaResponseDTO>> asistencias = asistenciaService.listarTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(asistencias,
                linkTo(methodOn(AsistenciaController.class).obtenerTodas()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener asistencia por id", description = "Obtiene una asistencia acorde a una id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Resenia no encontrada")
    })
    public ResponseEntity<Optional<AsistenciaResponseDTO>> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(asistenciaService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Obtener asistencias por usuario", description = "Obtiene una asistencia acorde a id de usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Asistencia no encontrada")
    })
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerPorUsuario(@PathVariable Long id){
        return ResponseEntity.ok(asistenciaService.listarPorUsuario(id));
    }

    @GetMapping("/taller/{id}")
    @Operation(summary = "Obtener asistencias por taller", description = "Obtiene una asistencia acorde a id de taller.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Asistencia no encontrada")
    })
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerPorTaller(@PathVariable Long id){
        return ResponseEntity.ok(asistenciaService.listarPorTaller(id));
    }

    @PostMapping
    @Operation(summary = "Guardar una asistencia", description = "Guarda una asistencia acorde a lo ingresado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa."),
            @ApiResponse(responseCode = "400", description = "Error al ingresar parametros. Revise si ingreso todos los parametros solicitados."),
            @ApiResponse(responseCode = "403", description = "No tienes permiso para hacer el cambio.")
    })
    public ResponseEntity<AsistenciaResponseDTO> guardar(@Valid @RequestBody AsistenciaRequestDTO doto){
        return ResponseEntity.status(201).body(asistenciaService.guardar(doto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar asistencia", description = "Actualiza una asistencia acorde a una id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asistencia actualizada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Asistencia.class))),
            @ApiResponse(responseCode = "404", description = "El id de la asistencia no existe.")
    })
    public ResponseEntity<AsistenciaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AsistenciaRequestDTO doto){
        return asistenciaService.actualizar(id, doto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar asistencia", description = "Elimina una asistencia acorde a una id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "¡Asistencia eliminada con exito!"),
            @ApiResponse(responseCode = "404",description = "ERROR: ¡El id de la asistencia ingresada no existe!")
    })
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
