package cl.duoc.mpazpro.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mpazpro.domain.Contenido
import cl.duoc.mpazpro.domain.EstadoRevisionUtp
import cl.duoc.mpazpro.domain.Materia
import cl.duoc.mpazpro.domain.SolicitudUtp
import cl.duoc.mpazpro.nativo.rememberReproductorAudio
import cl.duoc.mpazpro.ui.theme.Primario
import cl.duoc.mpazpro.viewmodel.AprendizajeViewModel
import cl.duoc.mpazpro.viewmodel.AppViewModelProvider

data class SolicitudRevision(
    val id: Int,
    val materia: String,
    val docente: String,
    val titulo: String,
    val descripcion: String,
    var estado: String = "PENDIENTE"
)

private fun Materia.colorAsignaturaUtp(): Color = when (this) {
    Materia.LENGUAJE -> Color(0xFFC62828)
    Materia.MATEMATICAS -> Color(0xFF0D5C9E)
    Materia.HISTORIA -> Color(0xFFE5B822)
    Materia.CIENCIAS -> Color(0xFF2E7D32)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UTPScreen(
    navPerfil: () -> Unit,
    vm: AprendizajeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by vm.utpState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.refrescarSolicitudesUtp()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jefatura UTP", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                actions = {
                    IconButton(onClick = navPerfil) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Primario),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Revisiones Curriculares",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Supervisión de Planificaciones",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${uiState.pendientes}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            )
                            Text(
                                text = if (uiState.pendientes == 1) "Pendiente" else "Pendientes",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 13.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${uiState.aprobadas}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            )
                            Text(
                                text = "Aprobadas",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Filtrar por asignatura:",
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipFiltroAsignatura(
                    texto = "Todas",
                    seleccionado = uiState.filtroMateria == null,
                    colorActivo = Primario,
                    onClick = { vm.seleccionarFiltroUtp(null) }
                )
                Materia.entries.forEach { materia ->
                    val nombreCorto = if (materia == Materia.CIENCIAS) "Ciencias" else materia.titulo
                    ChipFiltroAsignatura(
                        texto = nombreCorto,
                        seleccionado = uiState.filtroMateria == materia,
                        colorActivo = materia.colorAsignaturaUtp(),
                        onClick = { vm.seleccionarFiltroUtp(materia) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Solicitudes de Aprobación",
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(uiState.solicitudesFiltradas, key = { it.id }) { item ->
                    TarjetaSolicitudUtp(
                        item = item,
                        onAprobar = { vm.aprobarSolicitudUtp(item.id) },
                        onSolicitarCambios = { vm.solicitarCambiosUtp(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChipFiltroAsignatura(
    texto: String,
    seleccionado: Boolean,
    colorActivo: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(50),
        color = if (seleccionado) colorActivo else Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (seleccionado) colorActivo else colorActivo.copy(alpha = 0.45f)
        )
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
            fontSize = 13.sp,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
            color = if (seleccionado) Color.White else Color.Black
        )
    }
}

@Composable
private fun TarjetaSolicitudUtp(
    item: SolicitudUtp,
    onAprobar: () -> Unit,
    onSolicitarCambios: () -> Unit
) {
    val colorMateria = item.materia.colorAsignaturaUtp()
    val leccionAsociada = Contenido.lecciones(item.materia).find { it.numero == item.numeroLeccion }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.materia.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorMateria
                )

                when (item.estado) {
                    EstadoRevisionUtp.PENDIENTE -> {
                        Surface(
                            color = Color(0xFFDF8E2B),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "PENDIENTE",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    EstadoRevisionUtp.APROBADA -> {
                        Text(
                            text = "APROBADA",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF388E3C)
                        )
                    }
                    EstadoRevisionUtp.CAMBIOS_SOLICITADOS -> {
                        Surface(
                            color = Color(0xFFC62828).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "CAMBIOS SOLICITADOS",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (leccionAsociada != null) "${leccionAsociada.iconoVisual} ${item.titulo}" else item.titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Docente: ${item.docente}",
                fontSize = 13.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.descripcion,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f)
            )

            if (leccionAsociada != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = colorMateria.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, colorMateria.copy(alpha = 0.25f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Pregunta: ${leccionAsociada.pregunta}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A) ${leccionAsociada.opcionA}${if (leccionAsociada.respuestaCorrecta.equals("A", true)) " ✓" else ""}",
                            fontSize = 12.sp,
                            color = if (leccionAsociada.respuestaCorrecta.equals("A", true)) Color(0xFF2E7D32) else Color.DarkGray,
                            fontWeight = if (leccionAsociada.respuestaCorrecta.equals("A", true)) FontWeight.Bold else FontWeight.Normal
                        )
                        Text(
                            text = "B) ${leccionAsociada.opcionB}${if (leccionAsociada.respuestaCorrecta.equals("B", true)) " ✓" else ""}",
                            fontSize = 12.sp,
                            color = if (leccionAsociada.respuestaCorrecta.equals("B", true)) Color(0xFF2E7D32) else Color.DarkGray,
                            fontWeight = if (leccionAsociada.respuestaCorrecta.equals("B", true)) FontWeight.Bold else FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pista mascota: ${leccionAsociada.pistaMascota}",
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            if (item.estado == EstadoRevisionUtp.PENDIENTE) {
                Spacer(modifier = Modifier.height(14.dp))

                ReproductorCompactoUtp(claveLeccion = item.claveAudio)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onSolicitarCambios,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color(0xFFB71C1C)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB71C1C))
                    ) {
                        Text(
                            text = "Solicitar cambios",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = onAprobar,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Aprobar OA",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            } else if (item.estado == EstadoRevisionUtp.CAMBIOS_SOLICITADOS) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onAprobar) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = colorMateria,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Marcar como Aprobada", color = colorMateria, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReproductorCompactoUtp(claveLeccion: String) {
    val audio = rememberReproductorAudio(claveLeccion = claveLeccion)
    val duracionSegura = audio.duracionMs.coerceAtLeast(1)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFE6F2FA),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = audio.alternarPlayPause,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (audio.reproduciendo) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Reproducir o pausar",
                        tint = Primario
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Slider(
                    value = audio.posicionMs.toFloat().coerceIn(0f, duracionSegura.toFloat()),
                    onValueChange = { audio.buscarTiempo(it.toInt()) },
                    valueRange = 0f..duracionSegura.toFloat(),
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = Primario,
                        activeTrackColor = Primario,
                        inactiveTrackColor = Primario.copy(alpha = 0.25f)
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${formatearTiempoUtp(audio.posicionMs)} / ${formatearTiempoUtp(audio.duracionMs)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonPildoraAudioUtp(texto = "-5s") { audio.saltarSegundos(-5) }

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { audio.alternarPlayPause() },
                        shape = RoundedCornerShape(50),
                        color = Color.White.copy(alpha = 0.7f),
                        border = BorderStroke(1.dp, Primario)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (audio.reproduciendo) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Primario,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (audio.reproduciendo) "Pausar" else "Escuchar",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Primario
                            )
                        }
                    }

                    BotonPildoraAudioUtp(texto = "+5s") { audio.saltarSegundos(5) }
                }
            }
        }
    }
}

@Composable
private fun BotonPildoraAudioUtp(
    texto: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, Primario)
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Primario
        )
    }
}

private fun formatearTiempoUtp(milisegundos: Int): String {
    val totalSegundos = (milisegundos / 1000).coerceAtLeast(0)
    val horas = totalSegundos / 3600
    val minutos = (totalSegundos % 3600) / 60
    val segundos = totalSegundos % 60
    return if (horas > 0) {
        String.format("%02d:%02d:%02d", horas, minutos, segundos)
    } else {
        String.format("%02d:%02d", minutos, segundos)
    }
}