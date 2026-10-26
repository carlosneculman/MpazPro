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

data class PerfilFormUiState(
    val rol: Campo = Campo(),
    val curso: Campo = Campo(),
    val guardado: Boolean = false
)
data class UsuarioUiState(
    val nombre: String = "",
    val rol: String = "",
    val apellido: String = "",
    val correo: String = "",
    val curso: String = "",
    val fotoPath: String? = null,
    val recordatorios: Boolean = false
)

private fun Usuario?.aUi() = if (this == null) UsuarioUiState() else UsuarioUiState(
    nombre = this.nombre,
    rol = this.rol.name,
    correo = "contacto@mpaz.cl"
)

class PerfilViewModel(private val repo: UsuarioRepositorio) : ViewModel() {
    private val _form = MutableStateFlow(PerfilFormUiState())
    val form: StateFlow<PerfilFormUiState> = _form.asStateFlow()
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
    }

    fun guardar() {
        _form.update { it.copy(guardado = true) }
    }

    fun recordatorios(activo: Boolean) {
    }

    fun cerrarSesion() = repo.cerrarSesion()
}