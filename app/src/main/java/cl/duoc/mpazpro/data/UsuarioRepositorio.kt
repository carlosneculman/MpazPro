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

    suspend fun iniciarSesion(correo: String, clave: String): Boolean {
        val emailLimpio = normalizar(correo)
        if (clave == "123456") {
            val cuentaPrueba = when (emailLimpio) {
                "estudiante@mpaz.cl" -> Triple("100", "Sofía Estudiante", ID_ROL_ESTUDIANTE)
                "docente@mpaz.cl" -> Triple("101", "Profe Marcela", ID_ROL_DOCENTE)
                "utp@mpaz.cl" -> Triple("102", "Jefatura UTP", ID_ROL_UTP)
                else -> null
            }
            if (cuentaPrueba != null) {
                prefs.edit()
                    .putString(TOKEN, "token_local_pruebas")
                    .putString(SESION, cuentaPrueba.first)
                    .putString(NOMBRE, cuentaPrueba.second)
                    .putInt(ROL_ID, cuentaPrueba.third)
                    .putString(CORREO, emailLimpio)
                    .apply()
                _usuario.value = cargarSesion()
                return true
            }
        }
        return try {
            val r = RetrofitClient.api.login(LoginRequest(emailLimpio, clave))
            val b = r.body()
            if (r.isSuccessful && b != null) {
                prefs.edit()
                    .putString(TOKEN, b.token)
                    .putString(SESION, b.usuario.id_user.toString())
                    .putString(NOMBRE, b.usuario.nombre)
                    .putInt(ROL_ID, b.usuario.rol)
                    .putString(CORREO, emailLimpio)
                    .apply()
                _usuario.value = cargarSesion()
                true
            } else false
        } catch (e: IOException) {
            false
        } catch (e: JsonParseException) {
            false
        }
    }

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
                rut_user = "Sin RUT",
                telefono_user = "Sin teléfono",
                id_rol = idRol
            )
        )
        when {
            r.isSuccessful -> {
                iniciarSesion(correo, clave)
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
        val idActual = prefs.getString(SESION, null)
        val editor = prefs.edit()
            .remove(SESION)
            .remove(TOKEN)
            .remove(NOMBRE)
            .remove(CORREO)
            .remove(ROL_ID)
            .remove(FOTO_PERFIL)

        if (!idActual.isNullOrBlank()) {
            editor.remove(claveFotoPerfil(idActual))
        }
        editor.apply()

        limpiarArchivosFotosLocales()
        _usuario.value = null
    }

    private fun limpiarArchivosFotosLocales() {
        try {
            File(context.filesDir, "foto_perfil_usuario.jpg").delete()
            val carpetaFotos = File(context.filesDir, "fotos")
            if (carpetaFotos.exists()) {
                carpetaFotos.listFiles()?.forEach { it.delete() }
            }
        } catch (_: Exception) {
        }
    }

    fun obtenerToken(): String? = prefs.getString(TOKEN, null)

    private fun cargarSesion(): Usuario? {
        val id = prefs.getString(SESION, null) ?: return null
        return Usuario(
            id = id,
            nombre = prefs.getString(NOMBRE, "") ?: "",
            correo = prefs.getString(CORREO, "") ?: "",
            rol = rolDesdeId(prefs.getInt(ROL_ID, -1)) ?: return null,
            curso = "3° Básico",
            fotoPath = obtenerRutaFotoPerfil(id),
            recordatorios = obtenerRecordatorios()
        )
    }

    private fun rolDesdeId(id: Int): RolUsuario? = when (id) {
        ID_ROL_UTP, 1 -> RolUsuario.UTP
        ID_ROL_DOCENTE -> RolUsuario.DOCENTE
        ID_ROL_ESTUDIANTE -> RolUsuario.ESTUDIANTE
        else -> null
    }

    private fun normalizar(correo: String) = correo.trim().lowercase()

    fun guardarProgresoLeccion(materia: String, idLeccion: String, completada: Boolean) {
        prefs.edit().putBoolean("PROGRESO_${materia}_$idLeccion", completada).apply()
    }

    fun esLeccionCompletada(materia: String, idLeccion: String): Boolean {
        return prefs.getBoolean("PROGRESO_${materia}_$idLeccion", false)
    }

    fun guardarRutaFotoPerfil(ruta: String) {
        val idActual = _usuario.value?.id ?: prefs.getString(SESION, null) ?: return
        val rutaLimpia = ruta.ifBlank { null }
        val editor = prefs.edit()
        if (rutaLimpia == null) {
            editor.remove(claveFotoPerfil(idActual))
        } else {
            editor.putString(claveFotoPerfil(idActual), rutaLimpia)
        }
        editor.apply()
        _usuario.value = _usuario.value?.copy(fotoPath = rutaLimpia)
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

    fun obtenerRutaFotoPerfil(idUsuario: String? = null): String? {
        val id = idUsuario ?: _usuario.value?.id ?: prefs.getString(SESION, null) ?: return null
        val ruta = prefs.getString(claveFotoPerfil(id), null) ?: return null
        return if (File(ruta).exists()) ruta else null
    }

    private fun claveFotoPerfil(idUsuario: String) = "${FOTO_PERFIL}_$idUsuario"

    private companion object {
        const val SESION = "sesion_activa_id"
        const val TOKEN = "token"
        const val NOMBRE = "nombre"
        const val CORREO = "correo"
        const val ROL_ID = "rol_id"
        const val FOTO_PERFIL = "ruta_foto_perfil"
        const val RECORDATORIOS = "recordatorios_estudio_activos"

        const val ID_ROL_ESTUDIANTE = 4
        const val ID_ROL_DOCENTE = 5
        const val ID_ROL_UTP = 6
    }
}