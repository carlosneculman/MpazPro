package cl.duoc.mpazpro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.mpazpro.ui.theme.Primario

data class SolicitudRevision(
    val id: Int,
    val materia: String,
    val docente: String,
    val titulo: String,
    val descripcion: String,
    var estado: String = "PENDIENTE" // PENDIENTE, APROBADA, RECHAZADA
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UTPScreen(
    navPerfil: () -> Unit
) {
    var solicitudes by remember {
        mutableStateOf(
            mutableListOf(
                SolicitudRevision(1, "Lenguaje", "Prof. María González", "Comprensión Lectora OA1", "Lectura guiada y preguntas simples.", "PENDIENTE"),
                SolicitudRevision(2, "Matemáticas", "Prof. Carlos Ruiz", "Conteo del 1 al 20", "Actividad interactiva con material concreto.", "PENDIENTE"),
                SolicitudRevision(3, "Ciencias", "Prof. Ana Torres", "Los sentidos", "Ficha técnica y reconocimiento de texturas.", "APROBADA")
            )
        )
    }

    val pendientes = solicitudes.count { it.estado == "PENDIENTE" }
    val aprobadas = solicitudes.count { it.estado == "APROBADA" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jefatura UTP", fontWeight = FontWeight.Bold) },
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
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Revisiones Curriculares", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Supervisión de Planificaciones", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$pendientes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text("Pendientes", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$aprobadas", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text("Aprobadas", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("Solicitudes de Aprobación", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(solicitudes) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.materia.uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Primario
                                )
                                Text(
                                    text = item.estado,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (item.estado) {
                                        "APROBADA" -> Color(0xFF4CAF50)
                                        "RECHAZADA" -> Color(0xFFE53935)
                                        else -> Color(0xFFFB8C00)
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = item.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "Docente: ${item.docente}", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = item.descripcion, fontSize = 13.sp, color = Color.DarkGray)

                            if (item.estado == "PENDIENTE") {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            solicitudes = solicitudes.map {
                                                if (it.id == item.id) it.copy(estado = "RECHAZADA") else it
                                            }.toMutableList()
                                        },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935))
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Rechazar")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            solicitudes = solicitudes.map {
                                                if (it.id == item.id) it.copy(estado = "APROBADA") else it
                                            }.toMutableList()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Aprobar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}