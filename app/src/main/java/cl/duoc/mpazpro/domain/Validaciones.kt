package cl.duoc.mpazpro.domain

/** Reglas de validación puras (sin dependencias de Android, fáciles de testear). */
object Validaciones {
    const val CLAVE_MIN = 6

    private val REGEX_CORREO =
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")

    fun nombre(valor: String, etiqueta: String = "nombre"): String? = when {
        valor.isBlank() -> "Ingresa tu $etiqueta"
        valor.trim().length < 2 -> "Debe tener al menos 2 letras"
        valor.any { it.isDigit() } -> "No debe contener números"
        else -> null
    }

    fun correo(valor: String): String? = when {
        valor.isBlank() -> "Ingresa tu correo"
        !REGEX_CORREO.matches(valor.trim()) -> "Correo no válido"
        else -> null
    }

    fun clave(valor: String): String? = when {
        valor.isEmpty() -> "Ingresa una contraseña"
        valor.length < CLAVE_MIN -> "Mínimo $CLAVE_MIN caracteres"
        else -> null
    }

    fun repetir(clave: String, repetida: String): String? = when {
        repetida.isEmpty() -> "Repite la contraseña"
        clave != repetida -> "Las contraseñas no coinciden"
        else -> null
    }

    fun requerido(valor: String, mensaje: String): String? =
        if (valor.isBlank()) mensaje else null
}
