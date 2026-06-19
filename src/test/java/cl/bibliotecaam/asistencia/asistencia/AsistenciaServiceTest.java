package cl.bibliotecaam.asistencia.asistencia;

import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaRequestDTO;
import cl.bibliotecaam.asistencia.asistencia.dto.AsistenciaResponseDTO;
import cl.bibliotecaam.asistencia.asistencia.model.Asistencia;
import cl.bibliotecaam.asistencia.asistencia.repository.AsistenciaRepository;
import cl.bibliotecaam.asistencia.asistencia.service.AsistenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AsistenciaServiceTest {

    @Autowired
    private AsistenciaService asistenciaService;

    @MockitoBean
    private AsistenciaRepository asistenciaRepository;

    @MockitoBean(name = "webClientUsuario")
    private WebClient webClientUsuario;

    @MockitoBean(name = "webClientTaller")
    private WebClient webClientTaller;

    @BeforeEach
    public void limpiarMocks() {
        clearInvocations(asistenciaRepository);
    }

    @Test
    public void testFindAll(){
        when(asistenciaRepository.findAll()).thenReturn(List.of(new Asistencia(1L,1L,1L)));
        List<AsistenciaResponseDTO> asistencias = asistenciaService.listarTodas();
        assertNotNull(asistencias);
        assertEquals(1, asistencias.size());
    }

    @Test
    public void testFindById(){
        Asistencia asistencia = new Asistencia(1L,1L,1L);
        when(asistenciaRepository.findById(1L)).thenReturn(Optional.of(asistencia));
        Optional<AsistenciaResponseDTO> found = asistenciaService.obtenerPorId(1L);
        assertNotNull(found);
        assertEquals(1L, found.get().getIdAsistencia());
    }

    @Test
    public void testListarPorUsuario() {
        when(asistenciaRepository.findByIdUsuario(1L)).thenReturn(List.of(new Asistencia(1L, 1L, 1L)));
        List<AsistenciaResponseDTO> asistencias = asistenciaService.listarPorUsuario(1L);

        assertNotNull(asistencias);
        assertEquals(1, asistencias.size());
        assertEquals(1L, asistencias.get(0).getIdUsuario());
    }

    @Test
    public void testListarPorTaller() {
        when(asistenciaRepository.findByIdTaller(1L)).thenReturn(List.of(new Asistencia(1L, 1L, 1L)));
        List<AsistenciaResponseDTO> asistencias = asistenciaService.listarPorTaller(1L);

        assertNotNull(asistencias);
        assertEquals(1, asistencias.size());
        assertEquals(1L, asistencias.get(0).getIdTaller());
    }

    @Test
    public void testGuardar() {
        AsistenciaRequestDTO requestDTO = new AsistenciaRequestDTO(1L, 1L);

        simularWebClientExitoso(webClientUsuario);
        simularWebClientExitoso(webClientTaller);

        Asistencia asistenciaGuardada = new Asistencia(1L, 1L, 1L);
        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(asistenciaGuardada);

        AsistenciaResponseDTO resultado = asistenciaService.guardar(requestDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdAsistencia());
        verify(asistenciaRepository, times(1)).save(any(Asistencia.class));
    }

    @Test
    public void testActualizar() {
        AsistenciaRequestDTO requestDTO = new AsistenciaRequestDTO(2L, 2L);
        Asistencia asistenciaExistente = new Asistencia(1L, 1L, 1L);

        simularWebClientExitoso(webClientUsuario);
        simularWebClientExitoso(webClientTaller);

        when(asistenciaRepository.findById(1L)).thenReturn(Optional.of(asistenciaExistente));

        // El repositorio guarda y devuelve la asistencia con los datos nuevos
        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(new Asistencia(1L, 2L, 2L));

        Optional<AsistenciaResponseDTO> resultado = asistenciaService.actualizar(1L, requestDTO);

        assertTrue(resultado.isPresent());
        assertEquals(2L, resultado.get().getIdUsuario());
        assertEquals(2L, resultado.get().getIdTaller());
    }

    @Test
    public void testEliminarPorId() {
        doNothing().when(asistenciaRepository).deleteById(1L);

        asistenciaService.eliminarPorId(1L);

        verify(asistenciaRepository, times(1)).deleteById(1L);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private void simularWebClientExitoso(WebClient webClientMock) {
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientMock.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), any(Object[].class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("OK"));
    }
}