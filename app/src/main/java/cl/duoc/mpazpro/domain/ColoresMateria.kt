package cl.duoc.mpazpro.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Science
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ColoresMateria(
    val principal: Color,
    val completado: Color = Color(0xFF4CAF50),
    val bloqueado: Color = Color(0xFFF5F5F5)
){
    val colorFondo : Color get() = principal.copy(alpha = 0.1f)
}
enum class Materia(val titulo: String) {
    LENGUAJE("Lenguaje"),
    MATEMATICAS("Matemáticas"),
    HISTORIA("Historia"),
    CIENCIAS("Ciencias naturales");

    fun icono(): ImageVector = when (this){
        MATEMATICAS -> Icons.Default.Calculate
        LENGUAJE -> Icons.Default.Book
        CIENCIAS -> Icons.Default.Science
        HISTORIA -> Icons.Default.AccountBalance
    }
    fun colores(): ColoresMateria = when (this){
        MATEMATICAS -> ColoresMateria(Color(0xFF0056B3))
        LENGUAJE -> ColoresMateria(Color(0xFFC62828))
        CIENCIAS -> ColoresMateria(Color(0xFF2E7D32))
        HISTORIA -> ColoresMateria(Color(0xFFF5BE17))
    }
}

enum class EstadoLeccion { COMPLETADA, EN_CURSO, BLOQUEADA }

data class Leccion(val numero: Int, val titulo: String, val estado: EstadoLeccion)

object Contenido {
    fun lecciones(materia: Materia): List<Leccion> = when (materia) {
        Materia.LENGUAJE -> listOf(
            Leccion(1, "Las vocales", EstadoLeccion.COMPLETADA),
            Leccion(2, "Las sílabas", EstadoLeccion.COMPLETADA),
            Leccion(3, "Palabras simples", EstadoLeccion.EN_CURSO),
            Leccion(4, "Oraciones cortas", EstadoLeccion.BLOQUEADA),
            Leccion(5, "Lectura corta", EstadoLeccion.BLOQUEADA)
        )
        Materia.MATEMATICAS -> listOf(
            Leccion(1, "Números hasta 100", EstadoLeccion.COMPLETADA),
            Leccion(2, "Sumas simples", EstadoLeccion.EN_CURSO),
            Leccion(3, "Restas simples", EstadoLeccion.BLOQUEADA),
            Leccion(4, "Figuras geométricas", EstadoLeccion.BLOQUEADA),
            Leccion(5, "Medir y comparar", EstadoLeccion.BLOQUEADA)
        )
        Materia.HISTORIA -> listOf(
            Leccion(1, "Mi familia", EstadoLeccion.COMPLETADA),
            Leccion(2, "Mi escuela", EstadoLeccion.COMPLETADA),
            Leccion(3, "Mi comunidad", EstadoLeccion.COMPLETADA),
            Leccion(4, "Símbolos patrios", EstadoLeccion.EN_CURSO),
            Leccion(5, "Pueblos originarios", EstadoLeccion.BLOQUEADA)
        )
        Materia.CIENCIAS -> listOf(
            Leccion(1, "Seres vivos", EstadoLeccion.COMPLETADA),
            Leccion(2, "Las plantas", EstadoLeccion.COMPLETADA),
            Leccion(3, "Los animales", EstadoLeccion.COMPLETADA),
            Leccion(4, "El agua", EstadoLeccion.COMPLETADA),
            Leccion(5, "El cuerpo humano", EstadoLeccion.EN_CURSO)
        )
    }

    fun calcularProgreso(lecciones: List<Leccion>): Int {
        val completadas = lecciones.count { it.estado == EstadoLeccion.COMPLETADA }
        return if (lecciones.isNotEmpty()) (completadas * 100) / lecciones.size else 0
    }
}
