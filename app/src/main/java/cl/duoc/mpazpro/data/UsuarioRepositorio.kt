package cl.duoc.mpazpro.data

import android.content.Context
import android.content.SharedPreferences
import java.io.File
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Usuario(
    val nombre: String,
    val apellido: String,
    val correo: String,
    val rol: String? = null,
    val curso: String? = null,
    val fotoPath: String? = null,
    val recordatorios: Boolean = false
) {
    val perfilCompleto: Boolean get() = !rol.isNullOrBlank() && !curso.isNullOrBlank()
}

/**
 * Almacén local de usuarios y sesión (SharedPreferences).
 * Las contraseñas NUNCA se guardan en claro: se guarda sal + hash PBKDF2.
 *
 * Es la única fuente de verdad del usuario actual; todos los ViewModels la comparten,
 * así que lo que se guarda en una pantalla se ve en las demás.
 *
 * registrar / iniciarSesion / cambiarClave calculan un hash costoso a propósito:
 * llamarlos desde un hilo de fondo (Dispatchers.Default), nunca desde el hilo principal.
 */
class UsuarioRepositorio(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("mpazpro_usuarios", Context.MODE_PRIVATE)

    private val _usuario = MutableStateFlow(cargarSesion())

    /** Usuario con sesión iniciada, o null si no hay sesión. */
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    fun existe(correo: String): Boolean = prefs.contains(llave(normalizar(correo), "hash"))

    /** @return false si ya existe una cuenta con ese correo. */
    fun registrar(nombre: String, apellido: String, correo: String, clave: String): Boolean {
        val c = normalizar(correo)
        if (existe(c)) return false
        val editor = prefs.edit()
        editor.putString(llave(c, "nombre"), nombre.trim())
        editor.putString(llave(c, "apellido"), apellido.trim())
        guardarClave(editor, c, clave)
        editor.putString(SESION, c)
        editor.apply()
        _usuario.value = cargar(c)
        return true
    }

    /** @return true si las credenciales son correctas (y deja la sesión iniciada). */
    fun iniciarSesion(correo: String, clave: String): Boolean {
        val c = normalizar(correo)
        val salt = prefs.getString(llave(c, "salt"), null) ?: return false
        val esperado = prefs.getString(llave(c, "hash"), null) ?: return false
        val calculado = Base64.getEncoder().encodeToString(hash(clave, Base64.getDecoder().decode(salt)))
        if (!MessageDigest.isEqual(calculado.toByteArray(), esperado.toByteArray())) return false
        prefs.edit().putString(SESION, c).apply()
        _usuario.value = cargar(c)
        return true
    }

    /** @return false si no existe una cuenta con ese correo. */
    fun cambiarClave(correo: String, nueva: String): Boolean {
        val c = normalizar(correo)
        if (!existe(c)) return false
        val editor = prefs.edit()
        guardarClave(editor, c, nueva)
        editor.apply()
        return true
    }

    fun actualizarPerfil(rol: String, curso: String) = editarActual { c ->
        putString(llave(c, "rol"), rol)
        putString(llave(c, "curso"), curso)
    }

    fun actualizarFoto(ruta: String) {
        val anterior = _usuario.value?.fotoPath
        editarActual { c -> putString(llave(c, "foto"), ruta) }
        if (anterior != null && anterior != ruta) File(anterior).delete()
    }

    fun setRecordatorios(activo: Boolean) = editarActual { c ->
        putBoolean(llave(c, "recordatorios"), activo)
    }

    fun cerrarSesion() {
        prefs.edit().remove(SESION).apply()
        _usuario.value = null
    }

    // ---------------------------------------------------------------- internos

    private fun editarActual(cambios: SharedPreferences.Editor.(String) -> Unit) {
        val c = _usuario.value?.correo ?: return
        val editor = prefs.edit()
        editor.cambios(c)
        editor.apply()
        _usuario.value = cargar(c)
    }

    private fun cargarSesion(): Usuario? = prefs.getString(SESION, null)?.let { cargar(it) }

    private fun cargar(c: String): Usuario? {
        val nombre = prefs.getString(llave(c, "nombre"), null) ?: return null
        return Usuario(
            nombre = nombre,
            apellido = prefs.getString(llave(c, "apellido"), "").orEmpty(),
            correo = c,
            rol = prefs.getString(llave(c, "rol"), null),
            curso = prefs.getString(llave(c, "curso"), null),
            fotoPath = prefs.getString(llave(c, "foto"), null),
            recordatorios = prefs.getBoolean(llave(c, "recordatorios"), false)
        )
    }

    private fun guardarClave(editor: SharedPreferences.Editor, correo: String, clave: String) {
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        editor.putString(llave(correo, "salt"), Base64.getEncoder().encodeToString(salt))
        editor.putString(llave(correo, "hash"), Base64.getEncoder().encodeToString(hash(clave, salt)))
    }

    private fun hash(clave: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(clave.toCharArray(), salt, ITERACIONES, 256)
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
    }

    private fun normalizar(correo: String) = correo.trim().lowercase()

    private fun llave(correo: String, campo: String) = "u/$correo/$campo"

    private companion object {
        const val SESION = "sesion_activa"
        const val ITERACIONES = 100_000
    }
}
