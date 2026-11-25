package cl.duoc.mpazpro.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cl.duoc.mpazpro.data.UsuarioRepositorio
import cl.duoc.mpazpro.domain.Contenido
import cl.duoc.mpazpro.domain.EstadoRevisionUtp
import cl.duoc.mpazpro.domain.Materia
import cl.duoc.mpazpro.domain.SolicitudUtp
import cl.duoc.mpazpro.domain.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UtpUiState(
    val filtroMateria: Materia? = null,
    val solicitudes: List<SolicitudUtp> = emptyList()
) {
    val pendientes: Int
        get() = solicitudes.count { it.estado == EstadoRevisionUtp.PENDIENTE }
    val aprobadas: Int
        get() = solicitudes.count { it.estado == EstadoRevisionUtp.APROBADA }
    val solicitudesFiltradas: List<SolicitudUtp>
        get() = if (filtroMateria == null) {
            solicitudes
        } else {
            solicitudes.filter { it.materia == filtroMateria }
        }
}
class AprendizajeViewModel(private val repositorio: UsuarioRepositorio) : ViewModel() {
    var esCorrecta = mutableStateOf(false)
        private set
    var intentosRestantes = mutableStateOf(3)
        private set
    var mensajeFeedback = mutableStateOf("")
        private set
    private val _utpState = MutableStateFlow(
        UtpUiState(solicitudes = Contenido.obtenerSolicitudesUtp())
    )
    val utpState: StateFlow<UtpUiState> = _utpState.asStateFlow()
    fun verificarRespuesta(opcionElegida: String, respuestaCorrecta: String) {
        if (intentosRestantes.value <= 0 && !esCorrecta.value) return

        if (Validaciones.esRespuestaCorrecta(opcionElegida, respuestaCorrecta)) {
            esCorrecta.value = true
            mensajeFeedback.value = "¡Correcto! Respuesta acertada."
        } else {
            intentosRestantes.value -= 1
            esCorrecta.value = false

            if (intentosRestantes.value > 0) {
                mensajeFeedback.value = "Incorrecto. Intenta de nuevo (Quedan ${intentosRestantes.value} intentos)."
            } else {
                mensajeFeedback.value = "Te quedaste sin intentos."
            }
        }
    }
    fun guardarLeccion(materia: String, idLeccion: String) {
        if (esCorrecta.value) {
            repositorio.guardarProgresoLeccion(materia, idLeccion, completada = true)
        }
    }
    fun reiniciarLeccion() {
        esCorrecta.value = false
        intentosRestantes.value = 3
        mensajeFeedback.value = ""
    }
    fun refrescarSolicitudesUtp() {
        _utpState.update { it.copy(solicitudes = Contenido.obtenerSolicitudesUtp()) }
    }
    fun seleccionarFiltroUtp(materia: Materia?) {
        _utpState.update { it.copy(filtroMateria = materia) }
    }
    fun aprobarSolicitudUtp(id: String) {
        Contenido.actualizarEstadoSolicitudUtp(id, EstadoRevisionUtp.APROBADA)
        refrescarSolicitudesUtp()
    }
    fun solicitarCambiosUtp(id: String) {
        Contenido.actualizarEstadoSolicitudUtp(id, EstadoRevisionUtp.CAMBIOS_SOLICITADOS)
        refrescarSolicitudesUtp()
    }
}