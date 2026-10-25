package cl.duoc.mpazpro.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mpazpro.domain.Opciones
import cl.duoc.mpazpro.nativo.*
import cl.duoc.mpazpro.ui.components.*
import cl.duoc.mpazpro.ui.theme.ErrorRojo
import cl.duoc.mpazpro.ui.theme.Primario
import cl.duoc.mpazpro.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilInicialScreen(
    ok: () -> Unit,
    vol: (() -> Unit)? = null,
    vm: PerfilViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val ui by vm.form.collectAsStateWithLifecycle()
    val u by vm.usuario.collectAsStateWithLifecycle()
    LaunchedEffect(ui.guardado) { if (ui.guardado) ok() }
    val cam = rememberCamara(vm::onFoto)

    ContenidoCentrado {
        if (vol != null) {
            IconButton(onClick = vol, modifier = Modifier.offset(x = (-12).dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }
        Text("Completa tu perfil", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(Modifier.height(16.dp))

        Box(Modifier.fillMaxWidth(), Alignment.Center) {
            FotoPerfil(u.fotoPath, 100.dp, conInsigniaCamara = true, onClickCamara = cam)
        }
        TextButton(onClick = cam, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Tomar foto con la cámara", color = Primario)
        }
        Spacer(Modifier.height(16.dp))

        var expRol by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expRol, onExpandedChange = { expRol = it }) {
            OutlinedTextField(
                value = ui.rol.valor,
                onValueChange = {},
                readOnly = true,
                label = { Text("Rol") },
                isError = ui.rol.error != null,
                supportingText = { ui.rol.error?.let { Text(it, color = ErrorRojo) } },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expRol) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expRol, onDismissRequest = { expRol = false }) {
                Opciones.roles.forEach { r ->
                    DropdownMenuItem(text = { Text(r) }, onClick = { vm.onRol(r); expRol = false })
                }
            }
        }

        var expCurso by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expCurso, onExpandedChange = { expCurso = it }) {
            OutlinedTextField(
                value = ui.curso.valor,
                onValueChange = {},
                readOnly = true,
                label = { Text("Curso") },
                isError = ui.curso.error != null,
                supportingText = { ui.curso.error?.let { Text(it, color = ErrorRojo) } },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expCurso) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expCurso, onDismissRequest = { expCurso = false }) {
                Opciones.cursos.forEach { c ->
                    DropdownMenuItem(text = { Text(c) }, onClick = { vm.onCurso(c); expCurso = false })
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        BotonPrincipal("Guardar y continuar", vm::guardar)
    }
}

@Composable
fun PerfilScreen(
    ini: () -> Unit,
    sal: () -> Unit,
    vm: PerfilViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val u by vm.usuario.collectAsStateWithLifecycle()
    val noti = rememberPermisoNotificaciones { vm.recordatorios(it) }
    val cam = rememberCamara(vm::onFoto)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = ini,
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primario, selectedTextColor = Primario, indicatorColor = Color(0xFFE8EAF6))
                )
            }
        }
    ) { p ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(p)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Mi perfil", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            Text("Verifica que tus datos sean correctos", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(32.dp))

            FotoPerfil(u.fotoPath, 100.dp, conInsigniaCamara = true, onClickCamara = cam)
            Spacer(Modifier.height(16.dp))
            Text("${u.nombre} ${u.apellido}".trim(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(32.dp))

            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                FilaDatoPerfil("Nombre", "${u.nombre} ${u.apellido}".trim())
                HorizontalDivider(color = Color(0xFFEEEEEE))
                FilaDatoPerfil("Rol", u.rol ?: "—")
                HorizontalDivider(color = Color(0xFFEEEEEE))
                FilaDatoPerfil("Curso", u.curso ?: "—")
                HorizontalDivider(color = Color(0xFFEEEEEE))
                FilaDatoPerfil("Correo", u.correo)
                HorizontalDivider(color = Color(0xFFEEEEEE))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Recordatorios de estudio", fontSize = 14.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = u.recordatorios,
                        onCheckedChange = { if (it) noti() else vm.recordatorios(false) },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Primario)
                    )
                }
            }

            Spacer(Modifier.weight(1f))
            OutlinedButton(
                onClick = {
                    vm.cerrarSesion()
                    sal()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                border = BorderStroke(1.dp, ErrorRojo),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Cerrar sesión", color = ErrorRojo, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun FilaDatoPerfil(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, fontSize = 14.sp, color = Color.Gray)
        Text(valor.ifBlank { "—" }, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black)
    }
}