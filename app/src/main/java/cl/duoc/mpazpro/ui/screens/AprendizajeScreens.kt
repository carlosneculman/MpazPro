package cl.duoc.mpazpro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import cl.duoc.mpazpro.domain.*
import cl.duoc.mpazpro.ui.components.*
import cl.duoc.mpazpro.ui.theme.*

@Composable
fun InicioScreen(
    navM: (Materia) -> Unit,
    navP: () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(selected = true, onClick = {},
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Primario,
                        selectedTextColor = Primario, indicatorColor = Color(0xFFE8EAF6)))
                NavigationBarItem(selected = false, onClick = navP, icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Perfil") }, colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray))
            }
        }
    ) { p ->
        Column(Modifier.fillMaxSize().background(Color.White).padding(p).padding(horizontal = 24.dp)) {
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(36.dp).clip(CircleShape).background(Primario), Alignment.Center) {
                    Icon(Icons.Default.School, null, Modifier.size(20.dp), tint = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Escuela Especial", fontSize = 10.sp, color = Color.Gray)
                    Text("Marcela Paz", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primario)
                }
            }
            Spacer(Modifier.height(32.dp))

            Text("Hola, Estudiante", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("¿Qué quieres aprender hoy?", fontSize = 16.sp, color = Color.Gray)
            Spacer(Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(Materia.entries) { m ->
                    val c = m.colores()
                    val lecciones = Contenido.lecciones(m)
                    val pct = Contenido.calcularProgreso(lecciones)

                    Box(
                        Modifier.aspectRatio(0.85f).clip(RoundedCornerShape(24.dp)).background(c.principal).clickable { navM(m) }.padding(16.dp)
                    ) {
                        Column(Modifier.fillMaxSize()) {
                            Box(Modifier.size(48.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)), Alignment.Center) {
                                Icon(m.icono(), null, tint = Color.White, modifier = Modifier.size(24.dp))
                            }
                            Spacer(Modifier.weight(1f))
                            Text(m.titulo, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, lineHeight = 22.sp)
                            Spacer(Modifier.height(12.dp))
                            BarraProgreso(pct / 100f, Color.White)
                            Spacer(Modifier.height(8.dp))
                            Text("$pct% completado", color = Color.White, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriaScreen(
    materia: Materia,
    navAtras: () -> Unit,
    navLeccion: (String) -> Unit
) {
    val colores = materia.colores()
    val lecciones = Contenido.lecciones(materia)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(materia.titulo, color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navAtras) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colores.principal)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(lecciones) { leccion ->
                val estaBloqueada = leccion.estado == EstadoLeccion.BLOQUEADA

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !estaBloqueada) { navLeccion(leccion.numero.toString()) },
                    colors = CardDefaults.cardColors(containerColor = if (estaBloqueada) Color(0xFFEEEEEE) else Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (estaBloqueada) 0.dp else 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = when (leccion.estado) {
                            EstadoLeccion.COMPLETADA -> Icons.Default.Check
                            EstadoLeccion.BLOQUEADA -> Icons.Default.Lock
                            else -> Icons.Default.PlayArrow
                        }

                        val iconTint = if (estaBloqueada) Color.Gray else colores.principal
                        val bgTint = if (estaBloqueada) Color.LightGray.copy(alpha = 0.3f) else colores.principal.copy(alpha = 0.2f)

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(bgTint),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = null, tint = iconTint)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${leccion.numero}. ${leccion.titulo}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (estaBloqueada) Color.Gray else Color.Black
                            )
                            Text(
                                text = leccion.descripcion,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        if (!estaBloqueada) {
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeccionScreen(
    materia: Materia,
    idLeccion: String,
    navAtras: () -> Unit
) {
    val lecciones = Contenido.lecciones(materia)
    val leccion = lecciones.find { it.numero.toString() == idLeccion }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lección", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navAtras) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        if (leccion != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
                    .padding(24.dp)
            ) {
                Text(text = leccion.titulo, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = leccion.descripcion, fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = leccion.contenido,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        Contenido.marcarComoCompletada(materia, leccion.numero)
                        navAtras()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Primario)
                ) {
                    Text("Marcar como Completada")
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Lección no encontrada")
            }
        }
    }
}