package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.Campo
import cl.duoc.mpazpro.domain.Usuario
import cl.duoc.mpazpro.data.UsuarioRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

// Estado para los dropdowns y guardado (mantenido para no romper la UI)
data class PerfilFormUiState(
    val rol: Campo = Campo(),
    val curso: Campo = Campo(),
    val guardado: Boolean = false
)

// Estado adaptado al modelo simplificado del MVP
data class UsuarioUiState(
    val nombre: String = "",
    val rol: String = "",
    // Mantenemos estos campos por defecto para que PerfilScreen no marque error si los usa
    val apellido: String = "",
    val correo: String = "",
    val curso: String = "",
    val fotoPath: String? = null,
    val recordatorios: Boolean = false
)

// Mapeo seguro del nuevo dominio Usuario a la UI
private fun Usuario?.aUi() = if (this == null) UsuarioUiState() else UsuarioUiState(
    nombre = this.nombre,
    rol = this.rol.name,
    correo = "contacto@mpaz.cl" // Dato ficticio de relleno
)

class PerfilViewModel(private val repo: UsuarioRepositorio) : ViewModel() {

    private val _form = MutableStateFlow(PerfilFormUiState())
    val form: StateFlow<PerfilFormUiState> = _form.asStateFlow()

    // Los datos salen del repositorio compartido
    val usuario: StateFlow<UsuarioUiState> = repo.usuario
        .map { it.aUi() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), repo.usuario.value.aUi())

    fun onRol(v: String) {
        _form.update { it.copy(rol = Campo(v, valido = true)) }
    }

    fun onCurso(v: String) {
        _form.update { it.copy(curso = Campo(v, valido = true)) }
    }

    fun onFoto(ruta: String) {
        // Sin efecto en el MVP, mantenido para compatibilidad con la vista
    }

    fun guardar() {
        // En el MVP los usuarios vienen por defecto, solo simulamos guardado
        _form.update { it.copy(guardado = true) }
    }

    fun recordatorios(activo: Boolean) {
        // Sin efecto en el MVP
    }

    fun cerrarSesion() = repo.cerrarSesion()
}