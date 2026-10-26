package cl.duoc.mpazpro

data class Campo(
    val valor: String = "",
    val error: String? = null,
    val valido: Boolean = false
)

fun campoValidado(valor: String, error: String?): Campo = Campo(
    valor = valor,
    error = if (valor.isEmpty()) null else error,
    valido = valor.isNotEmpty() && error == null
)

fun Campo.revisado(error: String?): Campo = copy(
    error = error,
    valido = valor.isNotEmpty() && error == null
)
