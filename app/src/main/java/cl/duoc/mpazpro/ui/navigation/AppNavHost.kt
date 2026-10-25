package cl.duoc.mpazpro.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.mpazpro.data.UsuarioRepositorio
import cl.duoc.mpazpro.domain.RolUsuario
import cl.duoc.mpazpro.ui.screens.*

object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val RECUPERAR = "recuperar"
    const val PERFIL_INICIAL = "perfil_inicial"
    const val PERFIL = "perfil"
    const val UTP = "utp"
    const val DOCENTE = "docente"
    const val ESTUDIANTE = "estudiante"
    const val MATERIA_DETALLE = "materia_detalle"
    const val LECCION_DETALLE = "leccion_detalle"
}

/** Navega a [ruta] borrando todo el back stack */
private fun NavController.irLimpiando(ruta: String) {
    navigate(ruta) { popUpTo(graph.id) { inclusive = true } }
}

private fun obtenerRutaPorRol(rol: RolUsuario?): String {
    return when (rol) {
        RolUsuario.UTP -> Rutas.UTP
        RolUsuario.DOCENTE -> Rutas.DOCENTE
        RolUsuario.ESTUDIANTE -> Rutas.ESTUDIANTE
        else -> Rutas.LOGIN
    }
}

@Composable
fun AppNavHost(repositorio: UsuarioRepositorio) {
    val navController = rememberNavController()
    val destinoInicial = remember {
        val u = repositorio.usuario.value
        if (u == null) Rutas.BIENVENIDA else obtenerRutaPorRol(u.rol)
    }

    NavHost(navController = navController, startDestination = destinoInicial) {
        composable(Rutas.BIENVENIDA) {
            BienvenidaScreen(nav = { navController.navigate(Rutas.LOGIN) })
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                ok = {
                    val u = repositorio.usuario.value
                    if (u != null) {
                        navController.irLimpiando(obtenerRutaPorRol(u.rol))
                    }
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
            PerfilInicialScreen(
                ok = {
                    val rol = repositorio.usuario.value?.rol
                    navController.irLimpiando(obtenerRutaPorRol(rol))
                }
            )
        }

        composable(Rutas.UTP) {
            UTPScreen(
                navPerfil = { navController.navigate(Rutas.PERFIL) { launchSingleTop = true } }
            )
        }

        composable(Rutas.DOCENTE) {
            DocenteScreen(
                navPerfil = { navController.navigate(Rutas.PERFIL) { launchSingleTop = true } }
            )
        }

        composable(Rutas.ESTUDIANTE) {
            InicioScreen(
                navM = { materia -> navController.navigate("${Rutas.MATERIA_DETALLE}/${materia.name}") },
                navP = { navController.navigate(Rutas.PERFIL) { launchSingleTop = true } }
            )
        }

        composable("${Rutas.MATERIA_DETALLE}/{materiaId}") { backStackEntry ->
            val materiaId = backStackEntry.arguments?.getString("materiaId") ?: return@composable
            val materia = cl.duoc.mpazpro.domain.Materia.valueOf(materiaId)

            MateriaScreen(
                materia = materia,
                navAtras = { navController.popBackStack() },
                navLeccion = { idLeccion -> navController.navigate("${Rutas.LECCION_DETALLE}/${materia.name}/$idLeccion") }
            )
        }

        composable("${Rutas.LECCION_DETALLE}/{materiaId}/{idLeccion}") { backStackEntry ->
            val materiaId = backStackEntry.arguments?.getString("materiaId") ?: return@composable
            val idLeccion = backStackEntry.arguments?.getString("idLeccion") ?: return@composable
            val materia = cl.duoc.mpazpro.domain.Materia.valueOf(materiaId)

            LeccionScreen(
                materia = materia,
                idLeccion = idLeccion,
                navAtras = { navController.popBackStack() }
            )
        }

        composable(Rutas.PERFIL) {
            PerfilScreen(
                ini = { navController.popBackStack() },
                sal = {
                    repositorio.cerrarSesion()
                    navController.irLimpiando(Rutas.LOGIN)
                }
            )
        }
    }
}

@Composable
fun PantallaPlaceholder(texto: String, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = texto)
            Button(onClick = { navController.navigate(Rutas.PERFIL) { launchSingleTop = true } }) {
                Text("Ir al Perfil")
            }
        }
    }
}