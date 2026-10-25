package cl.duoc.mpazpro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.mpazpro.domain.*
import cl.duoc.mpazpro.ui.theme.Primario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocenteScreen(
    navPerfil: () -> Unit
) {
    var mostrarModalCrear by remember { mutableStateOf(false) }
    var mostrarInforme by remember { mutableStateOf(false) }
    var informeValidado by remember { mutableStateOf(false) }
    var mensajeDescarga by remember { mutableStateOf(false) }

    var listaLecciones by remember {
        mutableStateOf(Contenido.lecciones(Materia.LENGUAJE).toMutableList())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Docente", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { mostrarInforme = true }) {
                        Icon(Icons.Default.Assessment, contentDescription = "Informe Pedagógico", tint = Primario)
                    }
                    IconButton(onClick = navPerfil) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarModalCrear = true },
                containerColor = Primario,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Lección")
            }
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
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Gestión de Contenidos y Evaluaciones", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Asignatura: Lenguaje y Comunicación (3° Básico)", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Lecciones: ${listaLecciones.size}", color = Color.White, fontSize = 12.sp)
                        Text("En revisión UTP: 1", color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Lecciones del Curso", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                TextButton(onClick = { mostrarInforme = true }) {
                    Text("Ver Reporte Curso", color = Primario, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listaLecciones) { leccion ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Primario.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(leccion.numero.toString(), fontWeight = FontWeight.Bold, color = Primario)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(leccion.titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(leccion.descripcion, fontSize = 12.sp, color = Color.Gray)
                                leccion.objetivoRelacionado?.let {
                                    Text("OA: ${it.codigoOficial} - ${it.ejeCurricular}", fontSize = 10.sp, color = Primario, fontWeight = FontWeight.Medium)
                                }
                            }
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        // Modal para crear lección con asignación de OA e Intentos
        if (mostrarModalCrear) {
            CrearLeccionDialog(
                onDismiss = { mostrarModalCrear = false },
                onGuardar = { nuevaLeccion ->
                    listaLecciones.add(nuevaLeccion)
                    mostrarModalCrear = false
                }
            )
        }

        if (mostrarInforme) {
            AlertDialog(
                onDismissRequest = { mostrarInforme = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Analytics, contentDescription = null, tint = Primario)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Informe Pedagógico", fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Rendimiento del Curso (Tercero Básico A)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("• Promedio General: 85% de logro", fontSize = 13.sp)
                        Text("• Fortalezas: Comprensión de textos breves y vocales.", fontSize = 13.sp, color = Color(0xFF2E7D32))
                        Text("• Aspectos a Reforzar: Fluidez en lectura de oraciones cortas.", fontSize = 13.sp, color = Color(0xFFC62828))

                        Divider(color = Color.LightGray)

                        if (informeValidado) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Informe Validado Profesionalmente", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text("Estado: Pendiente de validación docente", fontSize = 12.sp, color = Color.Gray)
                        }

                        if (mensajeDescarga) {
                            Text("📄 Informe guardado en Descargas (PDF)", fontSize = 12.sp, color = Primario, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                confirmButton = {
                    Row {
                        if (!informeValidado) {
                            Button(
                                onClick = { informeValidado = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            ) {
                                Text("Validar Informe")
                            }
                        } else {
                            Button(
                                onClick = { mensajeDescarga = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Primario)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Descargar PDF")
                            }
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarInforme = false }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}

@Composable
fun CrearLeccionDialog(
    onDismiss: () -> Unit,
    onGuardar: (Leccion) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var intentos by remember { mutableStateOf("3") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Lección / Actividad", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título de la lección") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción / Instrucciones") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = intentos,
                    onValueChange = { intentos = it },
                    label = { Text("Intentos Permitidos en Prueba Final") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titulo.isNotBlank()) {
                        onGuardar(
                            Leccion(
                                numero = (6..99).random(),
                                titulo = titulo,
                                estado = EstadoLeccion.EN_CURSO,
                                objetivoRelacionado = ObjetivoAprendizaje("OA_GEN", "OA01", "Lenguaje", 1, "Lectura", "Objetivo oficial de lectura."),
                                descripcion = descripcion.ifBlank { "Contenido con $intentos intentos permitidos." }
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primario)
            ) {
                Text("Enviar a UTP")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}