package cl.duoc.mpazpro.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import java.io.File

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

    ContenidoCentrado {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (vol != null) {
                IconButton(
                    onClick = vol,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .offset(x = (-12).dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }
            Text("Completa tu perfil", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(Modifier.height(16.dp))

            SelectorFotoConPrevia(
                fotoGuardada = u.fotoPath,
                mostrarBotonesTexto = true,
                onConfirmarFoto = vm::onFoto
            )

            Spacer(Modifier.height(16.dp))

            var expRol by rememberSaveable { mutableStateOf(false) }
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

            Spacer(Modifier.height(8.dp))

            var expCurso by rememberSaveable { mutableStateOf(false) }
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

            Spacer(Modifier.height(24.dp))
            BotonPrincipal("Guardar y continuar", vm::guardar)
        }
    }
}

@Composable
fun PerfilScreen(
    ini: () -> Unit,
    sal: () -> Unit,
    mostrarAjustes: Boolean = false,
    navAjustes: () -> Unit = {},
    vm: PerfilViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val context = LocalContext.current
    val u by vm.usuario.collectAsStateWithLifecycle()
    val noti = rememberPermisoNotificaciones { concedido ->
        vm.recordatorios(concedido)
    }
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = false,
                    onClick = ini,
                    icon = { Icon(Icons.Default.Home, null, Modifier.size(28.dp)) },
                    label = { Text("Inicio", fontSize = 14.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                    )
                )
                if (mostrarAjustes) {
                    NavigationBarItem(
                        selected = false,
                        onClick = navAjustes,
                        icon = { Icon(Icons.Default.Settings, null, Modifier.size(28.dp)) },
                        label = { Text("Ajustes", fontSize = 14.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    )
                }
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Person, null, Modifier.size(28.dp)) },
                    label = { Text("Perfil", fontSize = 14.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                    )
                )
            }
        }
    ) { p ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(p)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Mi perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "Verifica que tus datos sean correctos",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            SelectorFotoConPrevia(
                fotoGuardada = u.fotoPath,
                mostrarBotonesTexto = false,
                onConfirmarFoto = vm::onFoto
            )

            Spacer(Modifier.height(16.dp))
            Text(
                "${u.nombre} ${u.apellido}".trim(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(24.dp))

            val colorDivisor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                FilaDatoPerfil("Nombre", "${u.nombre} ${u.apellido}".trim())
                HorizontalDivider(color = colorDivisor)
                FilaDatoPerfil("Rol", u.rol.ifBlank { "—" })
                HorizontalDivider(color = colorDivisor)
                FilaDatoPerfil("Curso", u.curso.ifBlank { "—" })
                HorizontalDivider(color = colorDivisor)
                FilaDatoPerfil("Correo", u.correo)
                HorizontalDivider(color = colorDivisor)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = if (u.recordatorios) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Recordatorios de estudio",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }
                    Switch(
                        checked = u.recordatorios,
                        onCheckedChange = { activar ->
                            if (activar) {
                                noti()
                            } else {
                                vm.recordatorios(false)
                                cancelarNotificacionRecordatorio(context)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
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
private fun SelectorFotoConPrevia(
    fotoGuardada: String?,
    mostrarBotonesTexto: Boolean,
    onConfirmarFoto: (String) -> Unit
) {
    var fotoTemporal by rememberSaveable { mutableStateOf<String?>(null) }
    var mostrarMenuOrigen by rememberSaveable { mutableStateOf(false) }

    val cam = rememberCamara { ruta ->
        fotoTemporal?.let { File(it).delete() }
        fotoTemporal = ruta
    }

    val gal = rememberGaleria { ruta ->
        fotoTemporal?.let { File(it).delete() }
        fotoTemporal = ruta
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.fillMaxWidth(), Alignment.Center) {
            FotoPerfil(
                fotoTemporal ?: fotoGuardada,
                100.dp,
                conInsigniaCamara = fotoTemporal == null,
                onClickCamara = { mostrarMenuOrigen = true }
            )
        }

        AnimatedVisibility(visible = fotoTemporal != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(
                    text = "¿Te gusta esta foto de perfil?",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledIconButton(
                        onClick = {
                            fotoTemporal?.let { File(it).delete() }
                            fotoTemporal = null
                        },
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = ErrorRojo)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Descartar foto",
                            tint = Color.White
                        )
                    }

                    FilledIconButton(
                        onClick = {
                            fotoTemporal?.let { rutaConfirmada ->
                                onConfirmarFoto(rutaConfirmada)
                            }
                            fotoTemporal = null
                        },
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Confirmar foto",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        if (mostrarBotonesTexto && fotoTemporal == null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = cam) {
                    Text("Tomar foto", color = Primario)
                }
                Text("|", color = Color.LightGray)
                TextButton(onClick = gal) {
                    Text("Elegir de galería", color = Primario)
                }
            }
        }
    }

    if (mostrarMenuOrigen) {
        AlertDialog(
            onDismissRequest = { mostrarMenuOrigen = false },
            title = { Text("Foto de perfil", fontWeight = FontWeight.Bold) },
            text = { Text("¿Desde dónde quieres subir tu foto?") },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarMenuOrigen = false
                        cam()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primario)
                ) {
                    Text("Cámara")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        mostrarMenuOrigen = false
                        gal()
                    },
                    border = BorderStroke(1.dp, Primario)
                ) {
                    Text("Galería", color = Primario)
                }
            },
            containerColor = Color.White
        )
    }
}

@Composable
private fun FilaDatoPerfil(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
        Text(valor.ifBlank { "—" }, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
    }
}