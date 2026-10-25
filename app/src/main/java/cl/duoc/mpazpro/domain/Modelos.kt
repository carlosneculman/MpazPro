package cl.duoc.mpazpro.domain

enum class RolUsuario { UTP, DOCENTE, ESTUDIANTE }

data class Usuario(
    val id: String,
    val nombre: String,
    val apellido: String = "",
    val correo: String = "",
    val rol: RolUsuario,
    val curso: String? = null,
    val fotoPath: String? = null,
    val recordatorios: Boolean = false
)

data class ObjetivoAprendizaje(
    val idOa: String,
    val codigoOficial: String,   // ej: "OA01"
    val asignatura: String,      // ej: "Lenguaje"
    val unidad: Int = 1,         // Máximo 1 unidad por materia
    val ejeCurricular: String,   // ej: "Lectura", "Números y Operaciones"
    val descripcion: String,
    val estadoAprobacion: Boolean = false
)

enum class EstadoLeccion { COMPLETADA, EN_CURSO, BLOQUEADA }

data class Leccion(
    val numero: Int,
    val titulo: String,
    var estado: EstadoLeccion,
    val objetivoRelacionado: ObjetivoAprendizaje? = null,
    val descripcion: String = "Módulo de aprendizaje interactivo con apoyo de audio.",
    val contenido: String = "Aquí irá el texto pedagógico y las instrucciones del docente.",
    val audioResId: Int? = null // Soporte multimedia (recursos de audio res/raw)
)

data class ResultadoEvaluacion(
    val estudianteId: String,
    val leccionNumero: Int,
    val intentosUsados: Int,
    val intentosMaximos: Int = 3,
    val puntajeObtenido: Int,
    val aprobada: Boolean,
    val respuestasCorrectas: List<Int>,
    val respuestasIncorrectas: List<Int>
)

object Contenido {
    private val oasEjemplo = mapOf(
        Materia.LENGUAJE to ObjetivoAprendizaje("OA1_L", "OA01", "Lenguaje", 1, "Lectura", "Leer de manera fluida textos breves."),
        Materia.MATEMATICAS to ObjetivoAprendizaje("OA1_M", "OA01", "Matemáticas", 1, "Números", "Demostrar que comprenden la adición y sustracción."),
        Materia.HISTORIA to ObjetivoAprendizaje("OA1_H", "OA01", "Historia", 1, "Sociedad", "Reconocer símbolos patrios y su significado."),
        Materia.CIENCIAS to ObjetivoAprendizaje("OA1_C", "OA01", "Ciencias", 1, "Biología", "Identificar los órganos de los sentidos.")
    )

    private val mapaLecciones: MutableMap<Materia, MutableList<Leccion>> = mutableMapOf(
        Materia.LENGUAJE to mutableListOf(
            Leccion(1, "Las vocales", EstadoLeccion.COMPLETADA, oasEjemplo[Materia.LENGUAJE]),
            Leccion(2, "Las sílabas", EstadoLeccion.COMPLETADA, oasEjemplo[Materia.LENGUAJE]),
            Leccion(3, "Palabras simples", EstadoLeccion.EN_CURSO, oasEjemplo[Materia.LENGUAJE]),
            Leccion(4, "Oraciones cortas", EstadoLeccion.BLOQUEADA, oasEjemplo[Materia.LENGUAJE]),
            Leccion(5, "Lectura corta", EstadoLeccion.BLOQUEADA, oasEjemplo[Materia.LENGUAJE])
        ),
        Materia.MATEMATICAS to mutableListOf(
            Leccion(1, "Números hasta 100", EstadoLeccion.COMPLETADA, oasEjemplo[Materia.MATEMATICAS]),
            Leccion(2, "Sumas simples", EstadoLeccion.EN_CURSO, oasEjemplo[Materia.MATEMATICAS]),
            Leccion(3, "Restas simples", EstadoLeccion.BLOQUEADA, oasEjemplo[Materia.MATEMATICAS]),
            Leccion(4, "Figuras geométricas", EstadoLeccion.BLOQUEADA, oasEjemplo[Materia.MATEMATICAS]),
            Leccion(5, "Medir y comparar", EstadoLeccion.BLOQUEADA, oasEjemplo[Materia.MATEMATICAS])
        ),
        Materia.HISTORIA to mutableListOf(
            Leccion(1, "Mi familia", EstadoLeccion.COMPLETADA, oasEjemplo[Materia.HISTORIA]),
            Leccion(2, "Mi escuela", EstadoLeccion.COMPLETADA, oasEjemplo[Materia.HISTORIA]),
            Leccion(3, "Mi comunidad", EstadoLeccion.COMPLETADA, oasEjemplo[Materia.HISTORIA]),
            Leccion(4, "Símbolos patrios", EstadoLeccion.EN_CURSO, oasEjemplo[Materia.HISTORIA]),
            Leccion(5, "Pueblos originarios", EstadoLeccion.BLOQUEADA, oasEjemplo[Materia.HISTORIA])
        ),
        Materia.CIENCIAS to mutableListOf(
            Leccion(1, "Seres vivos", EstadoLeccion.COMPLETADA, oasEjemplo[Materia.CIENCIAS]),
            Leccion(2, "Las plantas", EstadoLeccion.COMPLETADA, oasEjemplo[Materia.CIENCIAS]),
            Leccion(3, "Los animales", EstadoLeccion.COMPLETADA, oasEjemplo[Materia.CIENCIAS]),
            Leccion(4, "El agua", EstadoLeccion.COMPLETADA, oasEjemplo[Materia.CIENCIAS]),
            Leccion(5, "El cuerpo humano", EstadoLeccion.EN_CURSO, oasEjemplo[Materia.CIENCIAS])
        )
    )

    fun lecciones(materia: Materia): List<Leccion> = mapaLecciones[materia] ?: emptyList()

    fun marcarComoCompletada(materia: Materia, numeroLeccion: Int) {
        val lista = mapaLecciones[materia] ?: return
        val index = lista.indexOfFirst { it.numero == numeroLeccion }
        if (index != -1) {
            lista[index] = lista[index].copy(estado = EstadoLeccion.COMPLETADA)
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