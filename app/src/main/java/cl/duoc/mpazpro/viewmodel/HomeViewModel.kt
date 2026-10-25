package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.data.UsuarioRepositorio
import cl.duoc.mpazpro.domain.RolUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Cargando : HomeUiState()
    object Idle : HomeUiState()
    data class NavegarA(val ruta: String) : HomeUiState()
    data class Error(val mensaje: String) : HomeUiState()
}

class HomeViewModel(private val repositorio: UsuarioRepositorio) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    fun iniciarSesion(correo: String, clave: String) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Cargando
            val exito = repositorio.iniciarSesion(correo, clave)

            if (exito) {
                val usuarioActivo = repositorio.usuario.value

                val rutaDestino = when (usuarioActivo?.rol) {
                    RolUsuario.UTP -> "ruta_pantalla_utp"
                    RolUsuario.DOCENTE -> "ruta_pantalla_docente"
                    RolUsuario.ESTUDIANTE -> "ruta_pantalla_estudiante"
                    else -> "ruta_login"
                }
                _uiState.value = HomeUiState.NavegarA(rutaDestino)
            } else {
                _uiState.value = HomeUiState.Error("Credenciales incorrectas")
            }
        }
    }

    fun navegacionCompletada() {
        _uiState.value = HomeUiState.Idle
    }
}