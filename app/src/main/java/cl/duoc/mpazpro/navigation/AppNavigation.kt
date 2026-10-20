package cl.duoc.mpazpro.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.mpazpro.ui.screens.*
import cl.duoc.mpazpro.viewmodel.AuthViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "bienvenida") {
        composable("bienvenida") {
            BienvenidaScreen(navController) }
        composable("login") {
            LoginScreen(navController, authViewModel) }
        composable("registro") {
            RegistroScreen(navController, authViewModel) }
        composable("olvide_clave") {
            OlvideClaveScreen(navController) }
        composable("rol_curso") {
            RolCursoScreen(navController) }
        composable("inicio") {
            InicioScreen(navController) }
        composable("materia/{nombreMateria}") {
            backStackEntry ->
            val materia = backStackEntry.arguments?.getString("nombreMateria") ?: "Lenguaje"
            MateriaScreen(navController, materia)
        }
    }
}

@Composable
fun OlvideClaveScreen(x0: NavHostController) {
    TODO("Not yet implemented")
}

@Composable
fun RegistroScreen(x0: NavHostController, x1: AuthViewModel) {
    TODO("Not yet implemented")
}

@Composable
fun LoginScreen(x0: NavHostController, x1: AuthViewModel) {
    TODO("Not yet implemented")
}

@Composable
fun BienvenidaScreen(x0: NavHostController) {
    TODO("Not yet implemented")
}