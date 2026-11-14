package cl.duoc.mpazpro.data

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import cl.duoc.mpazpro.domain.RolUsuario
import cl.duoc.mpazpro.domain.Usuario
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream

class UsuarioRepositorio(private val context: Context) {

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
        delay(800)

        val correoNormalizado = normalizar(correo)
        val idUsuario = credencialesFicticias[correoNormalizado]

        if (idUsuario != null) {
            prefs.edit().putString(SESION, idUsuario).apply()
            _usuario.value = cargarSesion()
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
        val usuarioBase = usuariosFicticios.find { it.id == idGuardado }
        val fotoGuardada = obtenerRutaFotoPerfil()
        return usuarioBase?.copy(fotoPath = fotoGuardada)
    }

    private fun normalizar(correo: String) = correo.trim().lowercase()

    fun guardarProgresoLeccion(materia: String, idLeccion: String, completada: Boolean) {
        prefs.edit().putBoolean("PROGRESO_${materia}_$idLeccion", completada).apply()
    }

    fun esLeccionCompletada(materia: String, idLeccion: String): Boolean {
        return prefs.getBoolean("PROGRESO_${materia}_$idLeccion", false)
    }

    // Guarda la ruta entregada por el FileProvider de la cámara y actualiza el usuario activo
    fun guardarRutaFotoPerfil(ruta: String) {
        prefs.edit().putString(FOTO_PERFIL, ruta).apply()
        _usuario.value = _usuario.value?.copy(fotoPath = ruta)
    }

    fun guardarFotoEnDisco(bitmap: Bitmap): String? {
        return try {
            val archivo = File(context.filesDir, "foto_perfil_usuario.jpg")
            val streamOut = FileOutputStream(archivo)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, streamOut)
            streamOut.flush()
            streamOut.close()

            guardarRutaFotoPerfil(archivo.absolutePath)
            archivo.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun obtenerRutaFotoPerfil(): String? {
        return prefs.getString(FOTO_PERFIL, null)
    }

    private companion object {
        const val SESION = "sesion_activa_id"
        const val FOTO_PERFIL = "ruta_foto_perfil"
    }
}