package cl.duoc.mpazpro.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import cl.duoc.mpazpro.domain.Materia
import cl.duoc.mpazpro.ui.screens.InicioScreen
import org.junit.Rule
import org.junit.Test

class InicioScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun inicioScreen_muestra_saludo_mascota_y_asignaturas() {
        composeRule.setContent {
            InicioScreen(
                navM = {},
                navP = {},
                navA = {},
                mascotaEmoji = "🐶",
                mascotaNombre = "Bruno"
            )
        }

        composeRule.onNodeWithText("¡Hola, Estudiante!").assertIsDisplayed()
        composeRule.onNodeWithText("¿Qué quieres aprender hoy?").assertIsDisplayed()
        composeRule.onNodeWithText("Bruno").assertIsDisplayed()
        composeRule.onNodeWithText(Materia.LENGUAJE.titulo).assertIsDisplayed()
        composeRule.onNodeWithText(Materia.MATEMATICAS.titulo).assertIsDisplayed()
    }
}