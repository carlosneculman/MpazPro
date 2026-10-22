package cl.duoc.mpazpro

data class Campo(
    val valor: String = "",
    val error: String? = null,
    val valido: Boolean = false
)

/**
 * Campo con validación "en vivo": mientras esté vacío no se muestra error
 * (para no marcar en rojo un campo que el usuario aún no tocó).
 */
fun campoValidado(valor: String, error: String?): Campo = Campo(
    valor = valor,
    error = if (valor.isEmpty()) null else error,
    valido = valor.isNotEmpty() && error == null
)

/** Validación al enviar el formulario: muestra el error aunque el campo esté vacío. */
fun Campo.revisado(error: String?): Campo = copy(
    error = error,
    valido = valor.isNotEmpty() && error == null
)
