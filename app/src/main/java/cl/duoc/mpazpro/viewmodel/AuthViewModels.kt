package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.mpazpro.Campo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// --- LOGIN ---
data class LoginUiState(
    val correo: Campo = Campo(),
    val clave: Campo = Campo(),
    val cargando: Boolean = false,
    val error: String? = null,
    val ingreso: Boolean = false
)

class LoginViewModel : ViewModel() {
    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui.asStateFlow()

    fun onCorreo(v: String) { _ui.value = _ui.value.copy(correo = Campo(v)) }
    fun onClave(v: String) { _ui.value = _ui.value.copy(clave = Campo(v)) }
    fun ingresar() { /* Lógica de login a implementar después */ }
}

// --- REGISTRO ---
data class RegistroUiState(
    val nombre: Campo = Campo(),
    val apellido: Campo = Campo(),
    val correo: Campo = Campo(),
    val clave: Campo = Campo(),
    val repetir: Campo = Campo(),
    val cargando: Boolean = false,
    val registrado: Boolean = false
)

class RegistroViewModel : ViewModel() {
    private val _ui = MutableStateFlow(RegistroUiState())
    val ui: StateFlow<RegistroUiState> = _ui.asStateFlow()

    fun onNombre(v: String) { _ui.value = _ui.value.copy(nombre = Campo(v)) }
    fun onApellido(v: String) { _ui.value = _ui.value.copy(apellido = Campo(v)) }
    fun onCorreo(v: String) { _ui.value = _ui.value.copy(correo = Campo(v)) }
    fun onClave(v: String) { _ui.value = _ui.value.copy(clave = Campo(v)) }
    fun onRepetir(v: String) { _ui.value = _ui.value.copy(repetir = Campo(v)) }
    fun registrar() {

    }
}

// --- RECUPERAR CONTRASEÑA ---
data class RecuperarUiState(
    val correo: Campo = Campo(),
    val nueva: Campo = Campo(),
    val repetir: Campo = Campo(),
    val exito: Boolean = false
)

class RecuperarViewModel : ViewModel() {
    private val _ui = MutableStateFlow(RecuperarUiState())
    val ui: StateFlow<RecuperarUiState> = _ui.asStateFlow()

    fun onCorreo(v: String) { _ui.value = _ui.value.copy(correo = Campo(v)) }
    fun onNueva(v: String) { _ui.value = _ui.value.copy(nueva = Campo(v)) }
    fun onRepetir(v: String) { _ui.value = _ui.value.copy(repetir = Campo(v)) }
    fun confirmar() {

    }
}
