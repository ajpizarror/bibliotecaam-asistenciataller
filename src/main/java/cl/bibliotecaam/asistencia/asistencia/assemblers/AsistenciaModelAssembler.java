package cl.bibliotecaam.asistencia.asistencia.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import cl.bibliotecaam.asistencia.asistencia.controller.AsistenciaController;
import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaResponseDTO; // <- Importa tu DTO
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class AsistenciaModelAssembler implements RepresentationModelAssembler<AsistenciaResponseDTO, EntityModel<AsistenciaResponseDTO>> {

    @Override
    public EntityModel<AsistenciaResponseDTO> toModel(AsistenciaResponseDTO asistenciaDto){
        return EntityModel.of(asistenciaDto,
                linkTo(methodOn(AsistenciaController.class).obtenerPorId(asistenciaDto.getIdAsistencia())).withSelfRel(),
                linkTo(methodOn(AsistenciaController.class).obtenerTodas()).withRel("asistencias"));
    }
}