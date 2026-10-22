package cl.duoc.mpazpro.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mpazpro.ui.components.*
import cl.duoc.mpazpro.ui.theme.Primario
import cl.duoc.mpazpro.viewmodel.*

@Composable
fun BienvenidaScreen(nav: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Color(0xFFF4F6F9))) {
        Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.weight(1f))
            Box(Modifier.size(140.dp).clip(CircleShape).background(Primario), Alignment.Center) {
                Icon(Icons.Default.School, null, Modifier.size(70.dp), tint = Color.White)
            }
            Spacer(Modifier.height(32.dp))
            Text("Escuela Especial", fontSize = 18.sp, color = Color.Gray)
            Text("Marcela Paz", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Primario)
            Spacer(Modifier.height(16.dp))
            Text("Bienvenido a tu aprendizaje", fontSize = 16.sp, color = Color.DarkGray)
            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(Color(0xFFE53935), Color(0xFFFBC02D), Color(0xFF1E88E5), Color(0xFF43A047)).forEach { c ->
                    Box(Modifier.size(12.dp).clip(CircleShape).background(c))
                }
            }
            Spacer(Modifier.weight(1f))
            BotonPrincipal("Comenzar", nav)
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun LoginScreen(ok: () -> Unit, rec: () -> Unit, reg: () -> Unit, vm: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    LaunchedEffect(ui.ingreso) { if (ui.ingreso) ok() }

    Box(Modifier.fillMaxSize().background(Color.White)) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            Box(Modifier.size(80.dp).clip(CircleShape).background(Primario), Alignment.Center) {
                Icon(Icons.Default.School, null, Modifier.size(40.dp), tint = Color.White)
            }
            Spacer(Modifier.height(24.dp))
            Text("Iniciar sesión", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("Ingresa con tu correo y contraseña", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(32.dp))

            CampoTexto(ui.correo, vm::onCorreo, "Correo", Icons.Default.Email, tipo = KeyboardType.Email)
            Spacer(Modifier.height(16.dp))
            CampoTexto(ui.clave, vm::onClave, "Contraseña", Icons.Default.Lock, true)

            Spacer(Modifier.height(32.dp))
            BotonPrincipal(if (ui.cargando) "Ingresando…" else "Iniciar sesión", vm::ingresar, !ui.cargando)
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = rec) { Text("¿Olvidó su contraseña?", color = Primario) }
            TextButton(onClick = reg) { Text("¿No tienes cuenta? Crear cuenta", color = Primario) }

            Spacer(Modifier.height(24.dp))
            ui.error?.let { BannerError(it) }
        }
    }
}

@Composable
fun RegistroScreen(vol: () -> Unit, ok: () -> Unit, vm: RegistroViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    LaunchedEffect(ui.registrado) { if (ui.registrado) ok() }

    ContenidoCentrado {
        IconButton(onClick = vol, modifier = Modifier.offset(x = (-12).dp)) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
        Text("Crear cuenta", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(Modifier.height(8.dp))

        CampoTexto(ui.nombre, vm::onNombre, "Nombre", Icons.Default.Person)
        CampoTexto(ui.apellido, vm::onApellido, "Apellido", Icons.Default.Person)
        CampoTexto(ui.correo, vm::onCorreo, "Correo", Icons.Default.Email, tipo = KeyboardType.Email)
        CampoTexto(ui.clave, vm::onClave, "Contraseña", Icons.Default.Lock, true)
        CampoTexto(ui.repetir, vm::onRepetir, "Repetir contraseña", Icons.Default.Lock, true)

        Spacer(Modifier.height(16.dp))
        BotonPrincipal(if (ui.cargando) "Creando cuenta…" else "Crear cuenta", vm::registrar, !ui.cargando)
    }
}

@Composable
fun RecuperarScreen(vol: () -> Unit, vm: RecuperarViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    LaunchedEffect(ui.exito) {
        if (ui.exito) {
            Toast.makeText(contexto, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
            vol()
        }
    }

    ContenidoCentrado {
        IconButton(onClick = vol, modifier = Modifier.offset(x = (-12).dp)) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
        Text("Cambiar contraseña", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text("Te ayudamos a recuperar tu acceso", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.height(16.dp))

        CampoTexto(ui.correo, vm::onCorreo, "Correo", Icons.Default.Email, tipo = KeyboardType.Email)
        CampoTexto(ui.nueva, vm::onNueva, "Nueva contraseña", Icons.Default.Lock, true)
        CampoTexto(ui.repetir, vm::onRepetir, "Repetir nueva contraseña", Icons.Default.Lock, true)

        Spacer(Modifier.height(32.dp))
        BotonPrincipal("Confirmar", vm::confirmar, !ui.cargando)
    }
}

