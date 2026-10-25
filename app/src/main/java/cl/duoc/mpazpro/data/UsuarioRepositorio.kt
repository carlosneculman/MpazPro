package cl.duoc.mpazpro.data

import android.content.Context
import android.content.SharedPreferences
import cl.duoc.mpazpro.domain.RolUsuario
import cl.duoc.mpazpro.domain.Usuario
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsuarioRepositorio(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("mpazpro_usuarios", Context.MODE_PRIVATE)

    private val usuariosFicticios = listOf(
        Usuario(id = "1", nombre = "Paz Morales", rol = RolUsuario.UTP),
        Usuario(id = "2", nombre = "Profesor Juan", rol = RolUsuario.DOCENTE),
        Usuario(id = "3", nombre = "Estudiante Ana", rol = RolUsuario.ESTUDIANTE)
    )

    private val credencialesFicticias = mapOf(
        "utp@mpaz.cl" to "1",
        "docente@mpaz.cl" to "2",
        "estudiante@mpaz.cl" to "3"
    )

    private val _usuario = MutableStateFlow(cargarSesion())
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    suspend fun iniciarSesion(correo: String, clave: String): Boolean {
        delay(800) // Simulamos tiempo de red

        val correoNormalizado = normalizar(correo)
        val idUsuario = credencialesFicticias[correoNormalizado]

        if (idUsuario != null) {
            // Guardamos el ID en sesión
            prefs.edit().putString(SESION, idUsuario).apply()
            _usuario.value = usuariosFicticios.find { it.id == idUsuario }
            return true
        }
        return false
    }

    fun cerrarSesion() {
        prefs.edit().remove(SESION).apply()
        _usuario.value = null
    }

    private fun cargarSesion(): Usuario? {
        val idGuardado = prefs.getString(SESION, null)
        return usuariosFicticios.find { it.id == idGuardado }
    }

    private fun normalizar(correo: String) = correo.trim().lowercase()

    private companion object {
        const val SESION = "sesion_activa_id"
    }
}