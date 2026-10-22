package cl.duoc.mpazpro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.mpazpro.ui.screens.*

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                ok = { navController.navigate("inicio") },
                rec = { navController.navigate("recuperar") },
                reg = { navController.navigate("registro") }
            )
        }
        composable("registro") {
            RegistroScreen(
                vol = { navController.popBackStack() },
                ok = { navController.navigate("inicio") }
            )
        }
        composable("recuperar") {
            RecuperarScreen(
                vol = { navController.popBackStack() }
            )
        }
        composable("inicio") {
            InicioScreen(
                navM = { /* Lógica de materia */ },
                navP = { navController.navigate("perfil") }
            )
        }
        composable("perfil") {
            PerfilInicialScreen(
                vol = { navController.popBackStack() },
                ok = { navController.navigate("inicio") }
            )
        }
    }
}