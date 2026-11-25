package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.Campo
import cl.duoc.mpazpro.data.UsuarioRepositorio
import cl.duoc.mpazpro.data.remote.RetrofitClient
import cl.duoc.mpazpro.data.remote.UserImgRequest
import cl.duoc.mpazpro.domain.Usuario
import cl.duoc.mpazpro.nativo.base64ABytes
import cl.duoc.mpazpro.nativo.rutaImagenABase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PerfilFormUiState(
    val rol: Campo = Campo(),
    val curso: Campo = Campo(),
    val guardado: Boolean = false
)

data class UsuarioUiState(
    val id_user: Int = 0,
    val nombre: String = "",
    val rol: String = "",
    val apellido: String = "",
    val correo: String = "",
    val curso: String = "",
    val fotoPath: String? = null,
    val recordatorios: Boolean = false
)

private fun Usuario?.aUi(
    recordatoriosRespaldo: Boolean = false
) = if (this == null) {
    UsuarioUiState(
        fotoPath = null,
        recordatorios = recordatoriosRespaldo
    )
} else {
    UsuarioUiState(
        id_user = this.id.toIntOrNull() ?: 0,
        nombre = this.nombre,
        apellido = this.apellido,
        rol = this.rol.name,
        correo = this.correo.ifBlank { "contacto@mpaz.cl" },
        curso = this.curso ?: "3° Básico",
        fotoPath = this.fotoPath,
        recordatorios = this.recordatorios
    )
}

class PerfilViewModel(private val repo: UsuarioRepositorio) : ViewModel() {
    private val _form = MutableStateFlow(PerfilFormUiState())
    val form: StateFlow<PerfilFormUiState> = _form.asStateFlow()

    private val _fotoBytes = MutableStateFlow<ByteArray?>(null)
    val fotoBytes: StateFlow<ByteArray?> = _fotoBytes.asStateFlow()

    val usuario: StateFlow<UsuarioUiState> = repo.usuario
        .map { it.aUi(repo.obtenerRecordatorios()) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            repo.usuario.value.aUi(repo.obtenerRecordatorios())
        )

    init {
        viewModelScope.launch {
            repo.usuario.collectLatest { user ->
                _fotoBytes.value = null
                val idUser = user?.id?.toIntOrNull() ?: 0
                if (idUser > 0) {
                    cargarFotoRemota(idUser)
                }
            }
        }
    }

    private suspend fun cargarFotoRemota(idUser: Int) {
        try {
            val response = RetrofitClient.api.obtenerFotoPerfil(idUser)
            if (response.isSuccessful) {
                val base64 = response.body()?.imagen_base64
                _fotoBytes.value = base64ABytes(base64)
            } else {
                _fotoBytes.value = null
            }
        } catch (_: Exception) {
            _fotoBytes.value = null
        }
    }

    fun onRol(v: String) {
        _form.update { it.copy(rol = Campo(v, valido = true)) }
    }

    fun onCurso(v: String) {
        _form.update { it.copy(curso = Campo(v, valido = true)) }
    }

    fun onFoto(ruta: String) {
        repo.guardarRutaFotoPerfil(ruta)
        val idUser = repo.usuario.value?.id?.toIntOrNull() ?: 0
        viewModelScope.launch {
            val base64 = withContext(Dispatchers.IO) {
                rutaImagenABase64(ruta)
            } ?: return@launch

            _fotoBytes.value = base64ABytes(base64)

            if (idUser > 0) {
                try {
                    RetrofitClient.api.guardarFotoPerfil(
                        UserImgRequest(
                            id_user = idUser,
                            imagen_base64 = base64
                        )
                    )
                } catch (_: Exception) {
                }
            }
        }
    }
    fun guardar() {
        _form.update { it.copy(guardado = true) }
    }
    fun recordatorios(activo: Boolean) {
        repo.guardarRecordatorios(activo)
    }
    fun cerrarSesion() {
        _fotoBytes.value = null
        repo.guardarRutaFotoPerfil("")
        repo.cerrarSesion()
    }
}