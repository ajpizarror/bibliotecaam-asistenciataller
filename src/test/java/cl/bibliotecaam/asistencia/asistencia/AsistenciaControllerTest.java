package cl.bibliotecaam.asistencia.asistencia;

import cl.bibliotecaam.asistencia.asistencia.assemblers.AsistenciaModelAssembler;
import cl.bibliotecaam.asistencia.asistencia.controller.AsistenciaController;
import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaRequestDTO;
import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaResponseDTO;
import cl.bibliotecaam.asistencia.asistencia.service.AsistenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AsistenciaController.class)
@ActiveProfiles("test")
@Import(AsistenciaModelAssembler.class)
@DisplayName("Tests Unitarios - AsistenciaController")
class AsistenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private AsistenciaService asistenciaService;

    @Test
    @DisplayName("GIVEN: Existen asistencias WHEN: GET /api/bibliotecaam/asistencia THEN: Retorna 200 ok y la lista")
    void shouldReturnTodasLasAsistencias() throws Exception {
        AsistenciaResponseDTO asis1 = new AsistenciaResponseDTO();
        asis1.setIdAsistencia(1L);
        asis1.setIdUsuario(1L);
        asis1.setIdTaller(1L);

        AsistenciaResponseDTO asis2 = new AsistenciaResponseDTO();
        asis2.setIdAsistencia(2L);
        asis2.setIdUsuario(2L);
        asis2.setIdTaller(2L);

        List<AsistenciaResponseDTO> lista = Arrays.asList(asis1, asis2);

        Mockito.when(asistenciaService.listarTodas()).thenReturn(lista);

        mockMvc.perform(get("/api/bibliotecaam/asistencia")
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.asistenciaResponseDTOList.length()").value(2))
                .andExpect(jsonPath("$._embedded.asistenciaResponseDTOList[0].idAsistencia").value(1L));
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: GET /api/bibliotecaam/asistencia/{id} THEN: Retorna 200 OK y el DTO")
    void shouldReturnAsistenciaById() throws Exception {
        Long id = 1L;
        AsistenciaResponseDTO mockResponse = new AsistenciaResponseDTO();
        mockResponse.setIdAsistencia(id);
        mockResponse.setIdUsuario(1L);
        mockResponse.setIdTaller(1L);

        Mockito.when(asistenciaService.obtenerPorId(id)).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(get("/api/bibliotecaam/asistencia/{id}", id)
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAsistencia").value(id))
                .andExpect(jsonPath("$.idUsuario").value(1L));
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: GET /api/bibliotecaam/asistencia/usuario/{id} THEN: Retorna 200 OK y la lista")
    void shouldReturnAsistenciaByUsuario() throws Exception {
        Long idUsuario = 1L;
        AsistenciaResponseDTO mockResponse = new AsistenciaResponseDTO();
        mockResponse.setIdAsistencia(1L);
        mockResponse.setIdUsuario(idUsuario);
        mockResponse.setIdTaller(1L);

        List<AsistenciaResponseDTO> lista = Arrays.asList(mockResponse);

        Mockito.when(asistenciaService.listarPorUsuario(idUsuario)).thenReturn(lista);

        mockMvc.perform(get("/api/bibliotecaam/asistencia/usuario/{id}", idUsuario)
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.asistenciaResponseDTOList[0].idUsuario").value(idUsuario));
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: GET /api/bibliotecaam/asistencia/taller/{id} THEN: Retorna 200 OK y la lista")
    void shouldReturnAsistenciaByTaller() throws Exception {
        Long idTaller = 1L;
        AsistenciaResponseDTO mockResponse = new AsistenciaResponseDTO();
        mockResponse.setIdAsistencia(1L);
        mockResponse.setIdUsuario(1L);
        mockResponse.setIdTaller(idTaller);

        List<AsistenciaResponseDTO> lista = Arrays.asList(mockResponse);

        Mockito.when(asistenciaService.listarPorTaller(idTaller)).thenReturn(lista);

        mockMvc.perform(get("/api/bibliotecaam/asistencia/taller/{id}", idTaller)
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.asistenciaResponseDTOList[0].idTaller").value(idTaller));
    }

    @Test
    @DisplayName("GIVEN: Request válido WHEN: POST /api/bibliotecaam/asistencia THEN: Retorna 201 Created")
    void shouldCreateAsistencia() throws Exception {
        AsistenciaRequestDTO request = new AsistenciaRequestDTO();
        request.setIdUsuario(1L);
        request.setIdTaller(1L);

        AsistenciaResponseDTO mockResponse = new AsistenciaResponseDTO();
        mockResponse.setIdAsistencia(1L);
        mockResponse.setIdUsuario(1L);
        mockResponse.setIdTaller(1L);

        Mockito.when(asistenciaService.guardar(any(AsistenciaRequestDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/bibliotecaam/asistencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAsistencia").value(1L))
                .andExpect(jsonPath("$.idUsuario").value(1L));
    }

    @Test
    @DisplayName("GIVEN: ID y Request válido WHEN: PUT /api/bibliotecaam/asistencia/{id} THEN: Retorna 200 OK")
    void shouldUpdateAsistencia() throws Exception {
        Long id = 1L;
        AsistenciaRequestDTO request = new AsistenciaRequestDTO();
        request.setIdUsuario(2L);
        request.setIdTaller(2L);

        AsistenciaResponseDTO mockResponse = new AsistenciaResponseDTO();
        mockResponse.setIdAsistencia(id);
        mockResponse.setIdUsuario(2L);
        mockResponse.setIdTaller(2L);

        Mockito.when(asistenciaService.actualizar(eq(id), any(AsistenciaRequestDTO.class))).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(put("/api/bibliotecaam/asistencia/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(2L))
                .andExpect(jsonPath("$.idTaller").value(2L));
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: DELETE /api/bibliotecaam/asistencia/{id} THEN: Retorna 204 No Content")
    void shouldDeleteAsistencia() throws Exception {
        Long id = 1L;
        AsistenciaResponseDTO mockResponse = new AsistenciaResponseDTO();
        mockResponse.setIdAsistencia(id);
        mockResponse.setIdUsuario(1L);
        mockResponse.setIdTaller(1L);

        Mockito.when(asistenciaService.obtenerPorId(id)).thenReturn(Optional.of(mockResponse));
        Mockito.doNothing().when(asistenciaService).eliminarPorId(id);

        mockMvc.perform(delete("/api/bibliotecaam/asistencia/{id}", id))
                .andExpect(status().isNoContent());
    }
}