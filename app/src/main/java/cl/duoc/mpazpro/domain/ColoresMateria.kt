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
