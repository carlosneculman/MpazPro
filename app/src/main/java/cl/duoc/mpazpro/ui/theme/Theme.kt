package cl.duoc.mpazpro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Tema claro con la marca de la escuela.
 *
 * Toda la UI usa fondos blancos/grises fijos, así que el tema es solo claro. Antes se usaba
 * dynamic color + modo oscuro: en modo oscuro los textos sin color explícito (campos de texto,
 * menús, barra de navegación) quedaban blancos sobre fondo blanco y eran ilegibles.
 * Si algún día se quiere modo oscuro, primero hay que reemplazar los Color.White fijos
 * por MaterialTheme.colorScheme.*
 */
private val EsquemaClaro = lightColorScheme(
    primary = Primario,
    onPrimary = Color.White,
    secondary = Primario,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = ErrorRojo
)

@Composable
fun MpazProTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}
