package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AuthUiState(
    val correo: String = "",
    val clave: String = "",
    val nombre: String = "",
    val correoError: Boolean = false,
    val claveError: Boolean = false,
    val nombreError: Boolean = false
)

class AuthViewModel : ViewModel() {
    private val _estado = MutableStateFlow(AuthUiState())
    val estado: StateFlow<AuthUiState> = _estado.asStateFlow()

    fun onCorreoChange(nuevoCorreo: String) {
        _estado.update { it.copy(correo = nuevoCorreo, correoError = false) }
    }

    fun onClaveChange(nuevaClave: String) {
        _estado.update { it.copy(clave = nuevaClave, claveError = false) }
    }

    fun onNombreChange(nuevoNombre: String) {
        _estado.update { it.copy(nombre = nuevoNombre, nombreError = false) }
    }

    fun validarLogin(): Boolean {
        val correoInvalido = !_estado.value.correo.contains("@")
        val claveInvalida = _estado.value.clave.length < 6
        _estado.update { it.copy(correoError = correoInvalido, claveError = claveInvalida) }
        return !correoInvalido && !claveInvalida
    }

    fun validarRegistro(): Boolean {
        val nombreInvalido = _estado.value.nombre.isBlank()
        val correoInvalido = !_estado.value.correo.contains("@")
        val claveInvalida = _estado.value.clave.length < 6
        _estado.update {
            it.copy(nombreError = nombreInvalido, correoError = correoInvalido, claveError = claveInvalida)
        }
        return !nombreInvalido && !correoInvalido && !claveInvalida
    }
}