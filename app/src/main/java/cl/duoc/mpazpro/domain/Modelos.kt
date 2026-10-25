package cl.duoc.mpazpro.domain

enum class RolUsuario {
    UTP, DOCENTE, ESTUDIANTE
}

data class Usuario(
    val id: String,
    val nombre: String,
    val rol: RolUsuario
)

data class ObjetivoAprendizaje(
    val idOa: String,
    val codigoOficial: String,
    val asignatura: String,
    val unidad: Int,
    val ejeCurricular: String,
    val estadoAprobacion: Boolean = false
)

data class Actividad(
    val idActividad: String,
    val objetivoRelacionado: ObjetivoAprendizaje,
    val titulo: String,
    val instrucciones: String,
    val intentosPermitidos: Int = 3,
    val esPruebaFinal: Boolean = false
)

enum class EstadoLeccion {
    COMPLETADA, EN_CURSO, BLOQUEADA
}

data class Leccion(
    val numero: Int,
    val titulo: String,
    var estado: EstadoLeccion,
    val descripcion: String = "Módulo de aprendizaje interactivo.",
    val contenido: String = "Aquí irá el texto y material preparado por el docente para que el estudiante lea y practique."
)

object Contenido {
    private val mapaLecciones: MutableMap<Materia, MutableList<Leccion>> = mutableMapOf(
        Materia.LENGUAJE to mutableListOf(
            Leccion(1, "Las vocales", EstadoLeccion.COMPLETADA),
            Leccion(2, "Las sílabas", EstadoLeccion.COMPLETADA),
            Leccion(3, "Palabras simples", EstadoLeccion.EN_CURSO),
            Leccion(4, "Oraciones cortas", EstadoLeccion.BLOQUEADA),
            Leccion(5, "Lectura corta", EstadoLeccion.BLOQUEADA)
        ),
        Materia.MATEMATICAS to mutableListOf(
            Leccion(1, "Números hasta 100", EstadoLeccion.COMPLETADA),
            Leccion(2, "Sumas simples", EstadoLeccion.EN_CURSO),
            Leccion(3, "Restas simples", EstadoLeccion.BLOQUEADA),
            Leccion(4, "Figuras geométricas", EstadoLeccion.BLOQUEADA),
            Leccion(5, "Medir y comparar", EstadoLeccion.BLOQUEADA)
        ),
        Materia.HISTORIA to mutableListOf(
            Leccion(1, "Mi familia", EstadoLeccion.COMPLETADA),
            Leccion(2, "Mi escuela", EstadoLeccion.COMPLETADA),
            Leccion(3, "Mi comunidad", EstadoLeccion.COMPLETADA),
            Leccion(4, "Símbolos patrios", EstadoLeccion.EN_CURSO),
            Leccion(5, "Pueblos originarios", EstadoLeccion.BLOQUEADA)
        ),
        Materia.CIENCIAS to mutableListOf(
            Leccion(1, "Seres vivos", EstadoLeccion.COMPLETADA),
            Leccion(2, "Las plantas", EstadoLeccion.COMPLETADA),
            Leccion(3, "Los animales", EstadoLeccion.COMPLETADA),
            Leccion(4, "El agua", EstadoLeccion.COMPLETADA),
            Leccion(5, "El cuerpo humano", EstadoLeccion.EN_CURSO)
        )
    )

    fun lecciones(materia: Materia): List<Leccion> = mapaLecciones[materia] ?: emptyList()

    fun marcarComoCompletada(materia: Materia, numeroLeccion: Int) {
        val lista = mapaLecciones[materia] ?: return
        val index = lista.indexOfFirst { it.numero == numeroLeccion }
        if (index != -1) {
            // Marcar la actual como completada
            lista[index] = lista[index].copy(estado = EstadoLeccion.COMPLETADA)

            // Desbloquear la siguiente si estaba bloqueada
            if (index + 1 < lista.size && lista[index + 1].estado == EstadoLeccion.BLOQUEADA) {
                lista[index + 1] = lista[index + 1].copy(estado = EstadoLeccion.EN_CURSO)
            }
        }
    }

    fun calcularProgreso(lecciones: List<Leccion>): Int {
        val completadas = lecciones.count { it.estado == EstadoLeccion.COMPLETADA }
        return if (lecciones.isNotEmpty()) (completadas * 100) / lecciones.size else 0
    }
}