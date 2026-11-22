package cl.duoc.mpazpro.data

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import cl.duoc.mpazpro.data.remote.LoginRequest
import cl.duoc.mpazpro.data.remote.RegistroRequest
import cl.duoc.mpazpro.data.remote.RetrofitClient
import cl.duoc.mpazpro.domain.RolUsuario
import cl.duoc.mpazpro.domain.Usuario
import com.google.gson.JsonParseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UsuarioRepositorio(private val context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("mpazpro_usuarios", Context.MODE_PRIVATE)

    private val _usuario = MutableStateFlow(cargarSesion())
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    //autenticacion (backend)

    /** devuelve true si el login fue exitoso. Guarda token y datos de sesion. */
    suspend fun iniciarSesion(correo: String, clave: String): Boolean = try {
        val r = RetrofitClient.api.login(LoginRequest(normalizar(correo), clave))
        val b = r.body()
        if (r.isSuccessful && b != null) {
            prefs.edit()
                .putString(TOKEN, b.token)
                .putString(SESION, b.usuario.id_user)
                .putString(NOMBRE, b.usuario.nombre)
                .putInt(ROL_ID, b.usuario.rol)
                .putString(CORREO, normalizar(correo))
                .apply()
            _usuario.value = cargarSesion()
            true
        } else false
    } catch (e: IOException) {
        false // sin red / servidor apagado
    } catch (e: JsonParseException) {
        false // el servidor respondió algo que no es el JSON esperado
    }

    /**
     * Crea la cuenta en el backend y luego inicia sesión automáticamente.
     * Devuelve null si todo salió bien; si no, un mensaje de error para mostrar en pantalla.
     */
    suspend fun registrar(
        nombre: String,
        apellido: String,
        correo: String,
        clave: String,
        idRol: Int = ID_ROL_ESTUDIANTE
    ): String? = try {
        val r = RetrofitClient.api.registro(
            RegistroRequest(
                email = normalizar(correo),
                nombre_user = "${nombre.trim()} ${apellido.trim()}".trim(),
                contrasena = clave,
                rut_user = null,       // el formulario aún no pide RUT
                telefono_user = null,  // ni teléfono
                id_rol = idRol
            )
        )
        when {
            r.isSuccessful -> {
                iniciarSesion(correo, clave) // si falla, la navegación cae en el login
                null
            }
            r.code() == 409 -> "El correo ya se encuentra registrado"
            r.code() == 400 -> "Los datos ingresados no son válidos"
            else -> "No se pudo crear la cuenta. Intenta nuevamente"
        }
    } catch (e: IOException) {
        "Sin conexión con el servidor"
    } catch (e: JsonParseException) {
        "Respuesta inesperada del servidor"
    }

    fun cerrarSesion() {
        prefs.edit()
            .remove(SESION)
            .remove(TOKEN)
            .remove(NOMBRE)
            .remove(CORREO)
            .remove(ROL_ID)
            .apply()
        _usuario.value = null
    }

    /** Para un futuro interceptor de OkHttp que envíe "Authorization: Bearer <token>". */
    fun obtenerToken(): String? = prefs.getString(TOKEN, null)

    private fun cargarSesion(): Usuario? {
        val id = prefs.getString(SESION, null) ?: return null
        return Usuario(
            id = id,
            nombre = prefs.getString(NOMBRE, "") ?: "",
            correo = prefs.getString(CORREO, "") ?: "",
            rol = rolDesdeId(prefs.getInt(ROL_ID, -1)) ?: return null,
            curso = "3° Básico",
            fotoPath = obtenerRutaFotoPerfil(),
            recordatorios = obtenerRecordatorios()
        )
    }

    //ID rol backend
    private fun rolDesdeId(id: Int): RolUsuario? = when (id) {
        ID_ROL_UTP -> RolUsuario.UTP
        ID_ROL_DOCENTE -> RolUsuario.DOCENTE
        ID_ROL_ESTUDIANTE -> RolUsuario.ESTUDIANTE
        else -> null
    }

    private fun normalizar(correo: String) = correo.trim().lowercase()

    // datos locales

    fun guardarProgresoLeccion(materia: String, idLeccion: String, completada: Boolean) {
        prefs.edit().putBoolean("PROGRESO_${materia}_$idLeccion", completada).apply()
    }

    fun esLeccionCompletada(materia: String, idLeccion: String): Boolean {
        return prefs.getBoolean("PROGRESO_${materia}_$idLeccion", false)
    }

    fun guardarRutaFotoPerfil(ruta: String) {
        prefs.edit().putString(FOTO_PERFIL, ruta).apply()
        _usuario.value = _usuario.value?.copy(fotoPath = ruta)
    }

    fun guardarRecordatorios(activo: Boolean) {
        prefs.edit().putBoolean(RECORDATORIOS, activo).apply()
        _usuario.value = _usuario.value?.copy(recordatorios = activo)
    }

    fun obtenerRecordatorios(): Boolean {
        return prefs.getBoolean(RECORDATORIOS, false)
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
        const val TOKEN = "token"
        const val NOMBRE = "nombre"
        const val CORREO = "correo"
        const val ROL_ID = "rol_id"
        const val FOTO_PERFIL = "ruta_foto_perfil"
        const val RECORDATORIOS = "recordatorios_estudio_activos"

        // IDs
        const val ID_ROL_UTP = 1
        const val ID_ROL_DOCENTE = 2
        const val ID_ROL_ESTUDIANTE = 3
    }
}