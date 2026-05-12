package cl.bibliotecaam.asistencia.asistencia.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistenciaRequestDTO {
    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;
    @NotNull(message = "El taller es obligatorio")
    private Long idTaller;
}
