package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.Campo
import cl.duoc.mpazpro.campoValidado
import cl.duoc.mpazpro.data.UsuarioRepositorio
import cl.duoc.mpazpro.domain.Validaciones
import cl.duoc.mpazpro.revisado
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val correo: Campo = Campo(),
    val clave: Campo = Campo(),
    val cargando: Boolean = false,
    val error: String? = null,
    val ingreso: Boolean = false
)

class LoginViewModel(private val repo: UsuarioRepositorio) : ViewModel() {
    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui.asStateFlow()

    fun onCorreo(v: String) {
        _ui.update { it.copy(correo = campoValidado(v, Validaciones.correo(v)), error = null) }
    }

    fun onClave(v: String) {
        _ui.update { it.copy(clave = Campo(v), error = null) }
    }

    fun ingresar() {
        val s = _ui.value
        if (s.cargando) return

        val eCorreo = Validaciones.correo(s.correo.valor)
        val eClave = Validaciones.requerido(s.clave.valor, "Ingresa tu contraseña")
        if (eCorreo != null || eClave != null) {
            _ui.update { it.copy(correo = it.correo.revisado(eCorreo), clave = it.clave.revisado(eClave)) }
            return
        }

        _ui.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            // Llamamos a la función suspendida del repositorio MVP
            val ok = repo.iniciarSesion(s.correo.valor, s.clave.valor)
            _ui.update {
                if (ok) it.copy(cargando = false, ingreso = true)
                else it.copy(cargando = false, error = "Correo o contraseña incorrectos")
            }
        }
    }
}

data class RegistroUiState(
    val nombre: Campo = Campo(),
    val apellido: Campo = Campo(),
    val correo: Campo = Campo(),
    val clave: Campo = Campo(),
    val repetir: Campo = Campo(),
    val cargando: Boolean = false,
    val registrado: Boolean = false
)

class RegistroViewModel(private val repo: UsuarioRepositorio) : ViewModel() {
    private val _ui = MutableStateFlow(RegistroUiState())
    val ui: StateFlow<RegistroUiState> = _ui.asStateFlow()

    fun onNombre(v: String) {
        _ui.update { it.copy(nombre = campoValidado(v, Validaciones.nombre(v, "nombre"))) }
    }

    fun onApellido(v: String) {
        _ui.update { it.copy(apellido = campoValidado(v, Validaciones.nombre(v, "apellido"))) }
    }

    fun onCorreo(v: String) {
        _ui.update { it.copy(correo = campoValidado(v, Validaciones.correo(v))) }
    }

    fun onClave(v: String) {
        _ui.update {
            val repetir = if (it.repetir.valor.isEmpty()) it.repetir
            else campoValidado(it.repetir.valor, Validaciones.repetir(v, it.repetir.valor))
            it.copy(clave = campoValidado(v, Validaciones.clave(v)), repetir = repetir)
        }
    }

    fun onRepetir(v: String) {
        _ui.update { it.copy(repetir = campoValidado(v, Validaciones.repetir(it.clave.valor, v))) }
    }

    fun registrar() {
        val s = _ui.value
        if (s.cargando) return

        val eNombre = Validaciones.nombre(s.nombre.valor, "nombre")
        val eApellido = Validaciones.nombre(s.apellido.valor, "apellido")
        val eCorreo = Validaciones.correo(s.correo.valor)
        val eClave = Validaciones.clave(s.clave.valor)
        val eRepetir = Validaciones.repetir(s.clave.valor, s.repetir.valor)

        _ui.update {
            it.copy(
                nombre = it.nombre.revisado(eNombre),
                apellido = it.apellido.revisado(eApellido),
                correo = it.correo.revisado(eCorreo),
                clave = it.clave.revisado(eClave),
                repetir = it.repetir.revisado(eRepetir)
            )
        }
        if (listOf(eNombre, eApellido, eCorreo, eClave, eRepetir).any { it != null }) return

        _ui.update { it.copy(cargando = true) }
        viewModelScope.launch {
            delay(1000) // Simulamos carga de red para el MVP
            _ui.update { it.copy(cargando = false, registrado = true) }
        }
    }
}

data class RecuperarUiState(
    val correo: Campo = Campo(),
    val nueva: Campo = Campo(),
    val repetir: Campo = Campo(),
    val cargando: Boolean = false,
    val exito: Boolean = false
)

class RecuperarViewModel(private val repo: UsuarioRepositorio) : ViewModel() {
    private val _ui = MutableStateFlow(RecuperarUiState())
    val ui: StateFlow<RecuperarUiState> = _ui.asStateFlow()

    fun onCorreo(v: String) {
        _ui.update { it.copy(correo = campoValidado(v, Validaciones.correo(v))) }
    }

    fun onNueva(v: String) {
        _ui.update {
            val repetir = if (it.repetir.valor.isEmpty()) it.repetir
            else campoValidado(it.repetir.valor, Validaciones.repetir(v, it.repetir.valor))
            it.copy(nueva = campoValidado(v, Validaciones.clave(v)), repetir = repetir)
        }
    }

    fun onRepetir(v: String) {
        _ui.update { it.copy(repetir = campoValidado(v, Validaciones.repetir(it.nueva.valor, v))) }
    }

    fun confirmar() {
        val s = _ui.value
        if (s.cargando) return

        val eCorreo = Validaciones.correo(s.correo.valor)
        val eNueva = Validaciones.clave(s.nueva.valor)
        val eRepetir = Validaciones.repetir(s.nueva.valor, s.repetir.valor)

        _ui.update {
            it.copy(
                correo = it.correo.revisado(eCorreo),
                nueva = it.nueva.revisado(eNueva),
                repetir = it.repetir.revisado(eRepetir)
            )
        }
        if (eCorreo != null || eNueva != null || eRepetir != null) return

        _ui.update { it.copy(cargando = true) }
        viewModelScope.launch {
            delay(1000) // Simulamos proceso en MVP
            _ui.update { it.copy(cargando = false, exito = true) }
        }
    }
}