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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.mpazpro.ui.ajustes.AjustesPrincipalScreen
import cl.duoc.mpazpro.ui.ajustes.AjustesRepositorio
import cl.duoc.mpazpro.ui.ajustes.FiestasChileScreen
import cl.duoc.mpazpro.ui.ajustes.MiMascotaScreen
import cl.duoc.mpazpro.ui.ajustes.ModoInclusivoScreen
import cl.duoc.mpazpro.ui.ajustes.PiezasTrajeScreen

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
    const val AJUSTES = "ajustes"
    const val AJUSTES_INCLUSIVO = "ajustes_inclusivo"
    const val AJUSTES_MASCOTA = "ajustes_mascota"
    const val AJUSTES_FIESTAS = "ajustes_fiestas"
    const val AJUSTES_PIEZAS = "ajustes_piezas"
}

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
fun AppNavHost(
    repositorio: UsuarioRepositorio,
    ajustesRepositorio: AjustesRepositorio
) {
    val navController = rememberNavController()
    val destinoInicial = remember {
        val u = repositorio.usuario.value
        if (u == null) Rutas.BIENVENIDA else obtenerRutaPorRol(u.rol)
    }

    val usuarioActual by repositorio.usuario.collectAsState()
    val esEstudiante = usuarioActual?.rol == RolUsuario.ESTUDIANTE

    // MODIFICADO: lee en tiempo real la mascota elegida en Ajustes
    val ajustesEstado by ajustesRepositorio.estado.collectAsState()
    val mascotaEmojiActual = ajustesEstado.mascota.emoji
    val mascotaNombreActual = ajustesEstado.nombreMascota.ifBlank { "Bruno" }

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
            // MODIFICADO: pasa emoji y nombre real de la mascota al Inicio
            InicioScreen(
                navM = { materia -> navController.navigate("${Rutas.MATERIA_DETALLE}/${materia.name}") },
                navP = { navController.navigate(Rutas.PERFIL) { launchSingleTop = true } },
                navA = { navController.navigate(Rutas.AJUSTES) { launchSingleTop = true } },
                mascotaEmoji = mascotaEmojiActual,
                mascotaNombre = mascotaNombreActual
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

            // MODIFICADO: pasa emoji y nombre real de la mascota a las lecciones (sin tocar idUsuarioActual ni Pokémon)
            LeccionScreen(
                materia = materia,
                idLeccion = idLeccion,
                navAtras = { navController.popBackStack() },
                idUsuarioActual = usuarioActual?.id?.toIntOrNull() ?: 1,
                mascotaEmoji = mascotaEmojiActual,
                mascotaNombre = mascotaNombreActual
            )
        }

        composable(Rutas.PERFIL) {
            PerfilScreen(
                ini = {
                    if (esEstudiante) {
                        navController.navigate(Rutas.ESTUDIANTE) {
                            popUpTo(Rutas.ESTUDIANTE) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navController.popBackStack()
                    }
                },
                sal = {
                    repositorio.cerrarSesion()
                    navController.irLimpiando(Rutas.LOGIN)
                },
                mostrarAjustes = esEstudiante,
                navAjustes = {
                    navController.navigate(Rutas.AJUSTES) {
                        popUpTo(Rutas.ESTUDIANTE) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                idUsuarioActual = usuarioActual?.id?.toIntOrNull() ?: 1
            )
        }

        composable(Rutas.AJUSTES) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        NavigationBarItem(
                            selected = false,
                            onClick = {
                                navController.navigate(Rutas.ESTUDIANTE) {
                                    popUpTo(Rutas.ESTUDIANTE) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Default.Home, null, Modifier.size(28.dp)) },
                            label = { Text("Inicio", fontSize = 14.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        )
                        NavigationBarItem(
                            selected = true,
                            onClick = {},
                            icon = { Icon(Icons.Default.Settings, null, Modifier.size(28.dp)) },
                            label = { Text("Ajustes", fontSize = 14.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                            )
                        )
                        NavigationBarItem(
                            selected = false,
                            onClick = {
                                navController.navigate(Rutas.PERFIL) {
                                    popUpTo(Rutas.ESTUDIANTE) { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Default.Person, null, Modifier.size(28.dp)) },
                            label = { Text("Perfil", fontSize = 14.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        )
                    }
                }
            ) { padding ->
                Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                    AjustesPrincipalScreen(
                        ajustesRepositorio = ajustesRepositorio,
                        onIrAMascota = { navController.navigate(Rutas.AJUSTES_MASCOTA) },
                        onIrAModoInclusivo = { navController.navigate(Rutas.AJUSTES_INCLUSIVO) }
                    )
                }
            }
        }

        composable(Rutas.AJUSTES_INCLUSIVO) {
            ModoInclusivoScreen(
                ajustesRepositorio = ajustesRepositorio,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.AJUSTES_MASCOTA) {
            MiMascotaScreen(
                ajustesRepositorio = ajustesRepositorio,
                onVolver = { navController.popBackStack() },
                onIrAFiestas = { navController.navigate(Rutas.AJUSTES_FIESTAS) }
            )
        }

        composable(Rutas.AJUSTES_FIESTAS) {
            FiestasChileScreen(
                ajustesRepositorio = ajustesRepositorio,
                onVolver = { navController.popBackStack() },
                onIrAPiezasTraje = { navController.navigate(Rutas.AJUSTES_PIEZAS) }
            )
        }

        composable(Rutas.AJUSTES_PIEZAS) {
            PiezasTrajeScreen(
                ajustesRepositorio = ajustesRepositorio,
                onVolver = { navController.popBackStack() },
                onGuardarYVolver = {
                    navController.popBackStack(Rutas.AJUSTES, inclusive = false)
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