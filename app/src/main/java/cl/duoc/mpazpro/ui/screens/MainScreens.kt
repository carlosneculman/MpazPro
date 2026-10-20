package cl.duoc.mpazpro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolCursoScreen(navController: NavController) {
    var expandedRol by remember { mutableStateOf(false) }
    var selectedRol by remember { mutableStateOf("Estudiante") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Box {
            Box(
                modifier = Modifier.size(100.dp).background(Color(0xFFE8EAF6), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, modifier = Modifier.size(60.dp), tint = Color(0xFF3F51B5))
            }
            IconButton(
                onClick = { /* Intent de Cámara */ },
                modifier = Modifier.align(Alignment.BottomEnd).background(Color(0xFF3F51B5), CircleShape).size(36.dp)
            ) {
                Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        ExposedDropdownMenuBox(
            expanded = expandedRol,
            onExpandedChange = { expandedRol = !expandedRol }
        ) {
            OutlinedTextField(
                value = selectedRol,
                onValueChange = {},
                readOnly = true,
                label = { Text("Rol") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRol) },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedRol,
                onDismissRequest = { expandedRol = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Estudiante") },
                    onClick = { selectedRol = "Estudiante"; expandedRol = false }
                )
                DropdownMenuItem(
                    text = { Text("Profesor") },
                    onClick = { selectedRol = "Profesor"; expandedRol = false }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.navigate("inicio") },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("CONTINUAR")
        }
    }
}

@Composable
fun InicioScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, null) })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Person, null) })
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MateriaCard("LENGUAJE", Color(0xFFE53935), Modifier.weight(1f)) {
                    navController.navigate("materia/Lenguaje")
                }
                MateriaCard("MATEMÁTICA", Color(0xFF1E88E5), Modifier.weight(1f)) {
                    navController.navigate("materia/Matematica")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MateriaCard("HISTORIA", Color(0xFFFFB300), Modifier.weight(1f)) {
                    navController.navigate("materia/Historia")
                }
                MateriaCard("CIENCIAS", Color(0xFF43A047), Modifier.weight(1f)) {
                    navController.navigate("materia/Ciencias")
                }
            }
        }
    }
}

@Composable
fun MateriaCard(titulo: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            Box(
                modifier = Modifier.size(50.dp).background(Color.White.copy(alpha = 0.3f), CircleShape)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(titulo, color = Color.White, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 0.5f },
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                trackColor = Color.White.copy(0.3f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriaScreen(navController: NavController, materia: String) {
    val materiaInfo = when (materia) {
        "Lenguaje" -> Pair(Color(0xFFE53935), Icons.Default.Book)
        "Matematica" -> Pair(Color(0xFF1E88E5), Icons.Default.Calculate)
        "Historia" -> Pair(Color(0xFFFFB300), Icons.Default.AccountBalance)
        else -> Pair(Color(0xFF43A047), Icons.Default.Science)
    }
    val colorPrincipal = materiaInfo.first
    val icono = materiaInfo.second

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(materia) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(120.dp).background(colorPrincipal.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(80.dp).background(colorPrincipal, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icono, null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EtapaItem("Unidad 1", true, colorPrincipal)
                EtapaItem("Unidad 2", true, colorPrincipal)
                EtapaItem("Unidad 3", false, colorPrincipal)
                EtapaItem("Unidad 4", false, Color.Gray)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorPrincipal)
            ) {
                Text("CONTINUAR DESAFÍO")
            }
        }
    }
}

@Composable
fun EtapaItem(titulo: String, completada: Boolean, color: Color) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            val icon = if (completada) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.width(16.dp))
            Text(titulo, style = MaterialTheme.typography.bodyLarge)
        }
    }
}