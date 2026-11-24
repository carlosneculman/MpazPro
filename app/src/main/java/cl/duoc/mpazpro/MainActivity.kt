package cl.duoc.mpazpro

// ORIGINAL
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

// MODIFICADO
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import cl.duoc.mpazpro.ui.ajustes.AjustesRepositorio
import cl.duoc.mpazpro.ui.ajustes.generarColorSchemeInclusivo

// ORIGINAL
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(ColorAndroid.TRANSPARENT, ColorAndroid.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(ColorAndroid.TRANSPARENT, ColorAndroid.TRANSPARENT)
        )
        val repositorio = (application as MpazApplication).repositorio
        setContent {

            // MODIFICADO
            val ajustesRepositorio = remember { AjustesRepositorio(applicationContext) }
            val estadoAjustes by ajustesRepositorio.estado.collectAsState()
            val densidadActual = LocalDensity.current

            // ORIGINAL
            MpazProTheme {

                // MODIFICADO
                CompositionLocalProvider(
                    LocalDensity provides Density(
                        density = densidadActual.density,
                        fontScale = densidadActual.fontScale * estadoAjustes.escalaTexto
                    )
                ) {
                    MaterialTheme(
                        colorScheme = generarColorSchemeInclusivo(estadoAjustes),
                        typography = MaterialTheme.typography
                    ) {

                        // MODIFICADO (antes era Color.White)
                        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).safeDrawingPadding()) {
                            // MODIFICADO (antes era AppNavHost(repositorio))
                            AppNavHost(repositorio, ajustesRepositorio)
                        }
                    }
                }
            }
        }
    }
}
