package cl.duoc.mpazpro.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ColoresMateria(val principal: Color)

enum class Materia(val titulo: String) {
    MATEMATICAS("Matemáticas"),
    LENGUAJE("Lenguaje");

    fun icono(): ImageVector = Icons.Default.Book
    fun colores(): ColoresMateria = ColoresMateria(Color(0xFF0056B3))
}

object Contenido {
    fun lecciones(materia: Materia): List<String> {
        return listOf("Lección 1", "Lección 2")
    }
}
