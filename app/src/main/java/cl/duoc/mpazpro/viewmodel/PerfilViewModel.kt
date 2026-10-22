package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.mpazpro.Campo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Estado para los dropdowns y guardado
data class PerfilFormUiState(
    val rol: Campo = Campo(),
    val curso: Campo = Campo(),
    val guardado: Boolean = false
)

// Estado para mostrar los datos del usuario en la pantalla PerfilScreen
data class UsuarioUiState(
    val nombre: String? = "Estudiante",
    val apellido: String? = "Demo",
    val correo: String? = "correo@mpaz.cl",
    val rol: String? = null,
    val curso: String? = null,
    val fotoPath: String? = null,
    val recordatorios: Boolean = false
)

class PerfilViewModel : ViewModel() {
    private val _form = MutableStateFlow(PerfilFormUiState())
    val form: StateFlow<PerfilFormUiState> = _form.asStateFlow()

    private val _usuario = MutableStateFlow(UsuarioUiState())
    val usuario: StateFlow<UsuarioUiState> = _usuario.asStateFlow()

    fun onRol(v: String) {
        _form.value = _form.value.copy(rol = Campo(v))
    }
    fun onCurso(v: String) {
        _form.value = _form.value.copy(curso = Campo(v))
    }
    fun onFoto(ruta: String) {
        _usuario.value = _usuario.value.copy(fotoPath = ruta)
    }
    fun guardar() {
        _form.value = _form.value.copy(guardado = true)
    }
    fun recordatorios(activo: Boolean) {
        _usuario.value = _usuario.value.copy(recordatorios = activo)
    }
    fun cerrarSesion() {}
}