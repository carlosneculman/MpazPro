package cl.duoc.mpazpro

import android.graphics.Color as ColorAndroid
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cl.duoc.mpazpro.ui.navigation.AppNavHost
import cl.duoc.mpazpro.ui.theme.MpazProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fondo blanco fijo -> iconos de barras de estado/navegación siempre oscuros
        // (con el modo oscuro del sistema quedaban blancos sobre blanco).
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(ColorAndroid.TRANSPARENT, ColorAndroid.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(ColorAndroid.TRANSPARENT, ColorAndroid.TRANSPARENT)
        )
        val repositorio = (application as MpazApplication).repositorio
        setContent {
            MpazProTheme {
                Box(Modifier.fillMaxSize().background(Color.White).safeDrawingPadding()) {
                    AppNavHost(repositorio)
                }
            }
        }
    }
}
