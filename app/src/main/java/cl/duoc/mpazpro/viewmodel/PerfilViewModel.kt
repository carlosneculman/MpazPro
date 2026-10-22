package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.Campo
import cl.duoc.mpazpro.data.Usuario
import cl.duoc.mpazpro.data.UsuarioRepositorio
import cl.duoc.mpazpro.domain.Validaciones
import cl.duoc.mpazpro.revisado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

// Estado para los dropdowns y guardado
data class PerfilFormUiState(
    val rol: Campo = Campo(),
    val curso: Campo = Campo(),
    val guardado: Boolean = false
)

// Estado para mostrar los datos del usuario en la pantalla PerfilScreen
data class UsuarioUiState(
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val rol: String? = null,
    val curso: String? = null,
    val fotoPath: String? = null,
    val recordatorios: Boolean = false
)

private fun Usuario?.aUi() = if (this == null) UsuarioUiState() else UsuarioUiState(
    nombre = nombre, apellido = apellido, correo = correo, rol = rol,
    curso = curso, fotoPath = fotoPath, recordatorios = recordatorios
)

class PerfilViewModel(private val repo: UsuarioRepositorio) : ViewModel() {

    private val _form = MutableStateFlow(
        PerfilFormUiState(
            rol = repo.usuario.value?.rol.aCampo(),
            curso = repo.usuario.value?.curso.aCampo()
        )
    )
    val form: StateFlow<PerfilFormUiState> = _form.asStateFlow()

    // Los datos salen del repositorio compartido: lo que se guarda aquí se ve en todas las pantallas.
    val usuario: StateFlow<UsuarioUiState> = repo.usuario
        .map { it.aUi() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), repo.usuario.value.aUi())

    fun onRol(v: String) {
        _form.update { it.copy(rol = Campo(v, valido = true)) }
    }

    fun onCurso(v: String) {
        _form.update { it.copy(curso = Campo(v, valido = true)) }
    }

    fun onFoto(ruta: String) = repo.actualizarFoto(ruta)

    fun guardar() {
        val f = _form.value
        val eRol = Validaciones.requerido(f.rol.valor, "Selecciona tu rol")
        val eCurso = Validaciones.requerido(f.curso.valor, "Selecciona tu curso")
        if (eRol != null || eCurso != null) {
            _form.update { it.copy(rol = it.rol.revisado(eRol), curso = it.curso.revisado(eCurso)) }
            return
        }
        repo.actualizarPerfil(f.rol.valor, f.curso.valor)
        _form.update { it.copy(guardado = true) }
    }

    fun recordatorios(activo: Boolean) = repo.setRecordatorios(activo)

    fun cerrarSesion() = repo.cerrarSesion()
}

private fun String?.aCampo() = if (this == null || this.isBlank()) Campo() else Campo(this, valido = true)
