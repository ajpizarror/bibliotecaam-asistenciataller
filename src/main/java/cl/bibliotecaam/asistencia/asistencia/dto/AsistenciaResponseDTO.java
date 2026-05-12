package cl.bibliotecaam.asistencia.asistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaResponseDTO {
    private Long idAsistencia;
    private Long idUsuario;
    private Long idTaller;
}
