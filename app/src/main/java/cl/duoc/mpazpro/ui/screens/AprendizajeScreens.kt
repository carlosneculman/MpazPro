package cl.duoc.mpazpro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mpazpro.domain.*
import cl.duoc.mpazpro.ui.components.*
import cl.duoc.mpazpro.ui.theme.*
import cl.duoc.mpazpro.viewmodel.*

@Composable
fun InicioScreen(
    navM: (Materia) -> Unit,
    navP: () -> Unit, vm: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val ui by vm.ui.collectAsStateWithLifecycle()

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
            Text("Hola, ${ui.nombre.split(" ").firstOrNull() ?: ""}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("¿Qué quieres aprender hoy?", fontSize = 16.sp, color = Color.Gray)
            Spacer(Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(Materia.entries) { m ->
                    val c = m.colores()
                    val total = Contenido.lecciones(m).size
                    val hechas = ui.completadas[m] ?: 0
                    val pct = if (total > 0) hechas * 100 / total else 0

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