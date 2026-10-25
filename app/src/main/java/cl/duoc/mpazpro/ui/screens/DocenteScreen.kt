package cl.duoc.mpazpro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
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
    var listaLecciones by remember {
        mutableStateOf(Contenido.lecciones(Materia.LENGUAJE).toMutableList())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Docente", fontWeight = FontWeight.Bold) },
                actions = {
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
                    Text("Gestión de Contenidos", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Asignatura: Lenguaje y Comunicación", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total: ${listaLecciones.size}", color = Color.White, fontSize = 12.sp)
                        Text("En revisión UTP: 1", color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("Lecciones del Curso", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

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
                            }
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        if (mostrarModalCrear) {
            CrearLeccionDialog(
                onDismiss = { mostrarModalCrear = false },
                onGuardar = { nuevaLeccion ->
                    listaLecciones.add(nuevaLeccion)
                    mostrarModalCrear = false
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
                                descripcion = descripcion.ifBlank { "Contenido enviado a UTP para revisión." }
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