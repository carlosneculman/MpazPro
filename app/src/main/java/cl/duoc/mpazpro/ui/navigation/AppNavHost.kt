package cl.duoc.mpazpro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.mpazpro.data.UsuarioRepositorio
import cl.duoc.mpazpro.ui.screens.*

object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val RECUPERAR = "recuperar"
    const val PERFIL_INICIAL = "perfil_inicial"
    const val INICIO = "inicio"
    const val PERFIL = "perfil"
}

/** Navega a [ruta] borrando todo el back stack (así "Atrás" no vuelve al login ni a pantallas previas). */
private fun NavController.irLimpiando(ruta: String) {
    navigate(ruta) { popUpTo(graph.id) { inclusive = true } }
}

@Composable
fun AppNavHost(repositorio: UsuarioRepositorio) {
    val navController = rememberNavController()

    // Si ya hay sesión guardada se salta el login.
    val destinoInicial = remember {
        val u = repositorio.usuario.value
        when {
            u == null -> Rutas.BIENVENIDA
            u.perfilCompleto -> Rutas.INICIO
            else -> Rutas.PERFIL_INICIAL
        }
    }

    NavHost(navController = navController, startDestination = destinoInicial) {
        composable(Rutas.BIENVENIDA) {
            BienvenidaScreen(nav = { navController.navigate(Rutas.LOGIN) })
        }
        composable(Rutas.LOGIN) {
            LoginScreen(
                ok = {
                    val completo = repositorio.usuario.value?.perfilCompleto == true
                    navController.irLimpiando(if (completo) Rutas.INICIO else Rutas.PERFIL_INICIAL)
                },
                rec = { navController.navigate(Rutas.RECUPERAR) },
                reg = { navController.navigate(Rutas.REGISTRO) }
            )
        }
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                vol = { navController.popBackStack() },
                ok = { navController.irLimpiando(Rutas.PERFIL_INICIAL) }
            )
        }
        composable(Rutas.RECUPERAR) {
            RecuperarScreen(
                vol = { navController.popBackStack() }
            )
        }
        composable(Rutas.PERFIL_INICIAL) {
            // Es un paso obligatorio tras registrarse: no tiene flecha "volver".
            PerfilInicialScreen(
                ok = { navController.irLimpiando(Rutas.INICIO) }
            )
        }
        composable(Rutas.INICIO) {
            InicioScreen(
                navM = { /*TODO: PANTALLA DE LECCIONES DE LA MATERIA */ },
                navP = { navController.navigate(Rutas.PERFIL) { launchSingleTop = true } }
            )
        }
        composable(Rutas.PERFIL) {
            PerfilScreen(
                ini = { navController.popBackStack(Rutas.INICIO, inclusive = false) },
                sal = { navController.irLimpiando(Rutas.LOGIN) }

            )
        }
    }
}
