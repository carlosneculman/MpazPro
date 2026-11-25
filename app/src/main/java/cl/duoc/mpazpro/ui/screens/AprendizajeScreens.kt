package cl.duoc.mpazpro.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import cl.duoc.mpazpro.data.PokemonRepository
import cl.duoc.mpazpro.domain.*
import cl.duoc.mpazpro.nativo.rememberReproductorAudio
import cl.duoc.mpazpro.ui.components.*
import cl.duoc.mpazpro.ui.theme.*
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import cl.duoc.mpazpro.data.remote.MedallaDto
import cl.duoc.mpazpro.data.remote.RetrofitClient
import cl.duoc.mpazpro.model.Pokemon

@Composable
fun InicioScreen(
    navM: (Materia) -> Unit,
    navP: () -> Unit,
    navA: () -> Unit = {},
    mascotaEmoji: String = "🐶",
    mascotaNombre: String = "Copito"
) {
    val frasesMascota = remember {
        listOf("¡Vamos!", "¡Tú puedes!", "¡A jugar!", "¡Genial!")
    }
    var indiceFrase by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, null, Modifier.size(28.dp)) },
                    label = { Text("Inicio", fontSize = 14.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = navA,
                    icon = { Icon(Icons.Default.Settings, null, Modifier.size(28.dp)) },
                    label = { Text("Ajustes", fontSize = 14.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = navP,
                    icon = { Icon(Icons.Default.Person, null, Modifier.size(28.dp)) },
                    label = { Text("Perfil", fontSize = 14.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                    )
                )
            }
        }
    ) { p ->
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(p)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    Alignment.Center
                ) {
                    Icon(
                        Icons.Default.School,
                        null,
                        Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Escuela Escritora",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Text(
                        "Marcela Paz",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "¡Hola, Estudiante!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        "¿Qué quieres aprender hoy?",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
                    )
                }

                Spacer(Modifier.width(10.dp))

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                    modifier = Modifier.clickable {
                        indiceFrase = (indiceFrase + 1) % frasesMascota.size
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = frasesMascota[indiceFrase],
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = mascotaEmoji,
                            fontSize = 30.sp
                        )
                        Text(
                            text = mascotaNombre,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(Materia.entries) { m ->
                    val c = m.colores()
                    val lecciones = Contenido.lecciones(m)
                    val pct = Contenido.calcularProgreso(lecciones)

                    Box(
                        Modifier
                            .aspectRatio(0.95f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(c.principal)
                            .clickable { navM(m) }
                            .padding(16.dp)
                    ) {
                        Column(Modifier.fillMaxSize()) {
                            Box(
                                Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.25f)),
                                Alignment.Center
                            ) {
                                Icon(m.icono(), null, tint = Color.White, modifier = Modifier.size(24.dp))
                            }
                            Spacer(Modifier.weight(1f))
                            Text(m.titulo, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, lineHeight = 22.sp)
                            Spacer(Modifier.height(8.dp))
                            BarraProgreso(pct / 100f, Color.White)
                            Spacer(Modifier.height(6.dp))
                            Text("$pct% completado", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(materia.titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = navAtras) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colores.principal)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                    colors = CardDefaults.cardColors(
                        containerColor = if (estaBloqueada) {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ),
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
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(bgTint),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(26.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${leccion.numero}. ${leccion.titulo}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (estaBloqueada) Color.Gray else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = leccion.descripcion,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }
                        if (!estaBloqueada) {
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(28.dp))
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
    navAtras: () -> Unit,
    idUsuarioActual: Int = 1,
    mascotaEmoji: String = "🐶",
    mascotaNombre: String = "Copito"
) {
    val colores = materia.colores()
    val lecciones = Contenido.lecciones(materia)
    val leccion = lecciones.find { it.numero.toString() == idLeccion }

    var respuestaSeleccionada by rememberSaveable(idLeccion) { mutableStateOf<Int?>(null) }
    var mostrarFeedback by rememberSaveable(idLeccion) { mutableStateOf(false) }
    var enviandoMedalla by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val pokemonRepo = remember { PokemonRepository(context) }
    var pokemonGanado by remember { mutableStateOf<Pokemon?>(null) }

    val letraSeleccionada = when (respuestaSeleccionada) {
        1 -> "A"
        2 -> "B"
        else -> ""
    }
    val esCorrecta = leccion != null && respuestaSeleccionada != null &&
            Validaciones.esRespuestaCorrecta(
                seleccion = letraSeleccionada,
                respuestaCorrecta = leccion.respuestaCorrecta
            )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "${materia.titulo} · Lección ${leccion?.numero ?: ""}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navAtras) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colores.principal)
            )
        }
    ) { padding ->
        if (leccion != null) {
            val claveAudioLeccion = "${materia.name}_${leccion.numero}"

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(20.dp)
            ) {
                Text(
                    text = leccion.titulo,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))

                TarjetaReproductorAudio(
                    titulo = "Audio explicativo: ${leccion.titulo}",
                    claveLeccion = claveAudioLeccion
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = colores.principal.copy(alpha = 0.10f)),
                    border = BorderStroke(1.5.dp, colores.principal.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = leccion.iconoVisual,
                            fontSize = 44.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = leccion.contenido,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.5.dp, colores.principal),
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = mascotaEmoji, fontSize = 30.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, colores.principal.copy(alpha = 0.4f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Pista de $mascotaNombre:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colores.principal
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = leccion.pistaMascota,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Pregunta de comprobación:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = colores.principal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = leccion.pregunta,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        val colorFondoA = when {
                            respuestaSeleccionada == 1 && esCorrecta -> Color(0xFFE8F5E9)
                            respuestaSeleccionada == 1 && !esCorrecta -> Color(0xFFFFEBEE)
                            else -> MaterialTheme.colorScheme.surface
                        }
                        val colorBordeA = when {
                            respuestaSeleccionada == 1 && esCorrecta -> Color(0xFF2E7D32)
                            respuestaSeleccionada == 1 && !esCorrecta -> Color(0xFFC62828)
                            else -> colores.principal.copy(alpha = 0.4f)
                        }

                        val colorFondoB = when {
                            respuestaSeleccionada == 2 && esCorrecta -> Color(0xFFE8F5E9)
                            respuestaSeleccionada == 2 && !esCorrecta -> Color(0xFFFFEBEE)
                            else -> MaterialTheme.colorScheme.surface
                        }
                        val colorBordeB = when {
                            respuestaSeleccionada == 2 && esCorrecta -> Color(0xFF2E7D32)
                            respuestaSeleccionada == 2 && !esCorrecta -> Color(0xFFC62828)
                            else -> colores.principal.copy(alpha = 0.4f)
                        }

                        OutlinedButton(
                            onClick = {
                                respuestaSeleccionada = 1
                                mostrarFeedback = true
                            },
                            enabled = !mostrarFeedback,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.5.dp, colorBordeA),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = colorFondoA,
                                disabledContainerColor = colorFondoA
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(colores.principal),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = leccion.opcionA,
                                    fontSize = 15.sp,
                                    color = if (respuestaSeleccionada == 1) Color.Black else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = {
                                respuestaSeleccionada = 2
                                mostrarFeedback = true
                            },
                            enabled = !mostrarFeedback,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.5.dp, colorBordeB),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = colorFondoB,
                                disabledContainerColor = colorFondoB
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(colores.principal),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("B", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = leccion.opcionB,
                                    fontSize = 15.sp,
                                    color = if (respuestaSeleccionada == 2) Color.Black else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        AnimatedVisibility(visible = mostrarFeedback) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                if (esCorrecta) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "¡Correcto! Respuesta acertada.",
                                            fontSize = 14.sp,
                                            color = Color(0xFF2E7D32),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Cancel, contentDescription = null, tint = Color(0xFFC62828))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Respuesta incorrecta. Puedes intentarlo nuevamente.",
                                                fontSize = 14.sp,
                                                color = Color(0xFFC62828),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Button(
                                            onClick = {
                                                respuestaSeleccionada = null
                                                mostrarFeedback = false
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Intentar de nuevo", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        Contenido.marcarComoCompletada(materia, leccion.numero)
                        enviandoMedalla = true
                        scope.launch {
                            val poke = pokemonRepo.obtenerPokemonAleatorio()
                            try {
                                RetrofitClient.api.desbloquearMedalla(
                                    MedallaDto(
                                        id_user = idUsuarioActual,
                                        materia = materia.name,
                                        numero_leccion = leccion.numero,
                                        id_pokemon = poke.id ?: 25,
                                        nombre_pokemon = poke.nombreFormateado
                                    )
                                )
                            } catch (_: Exception) {
                            } finally {
                                enviandoMedalla = false
                                pokemonGanado = poke
                            }
                        }
                    },
                    enabled = esCorrecta && !enviandoMedalla,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primario,
                        disabledContainerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (enviandoMedalla) "Guardando medalla..." else "Marcar como Completada",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (esCorrecta && !enviandoMedalla) Color.White else Color.DarkGray
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Lección no encontrada")
            }
        }

        pokemonGanado?.let { poke ->
            val tiposTexto = poke.types
                ?.mapNotNull { it.type?.name?.replaceFirstChar { c -> c.uppercase() } }
                ?.joinToString(" / ")

            AlertDialog(
                onDismissRequest = {
                    pokemonGanado = null
                    navAtras()
                },
                containerColor = Color.White,
                title = {
                    Text("¡Nueva Medalla Pokémon!", fontWeight = FontWeight.ExtraBold, color = Primario)
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = poke.imageUrl,
                            contentDescription = poke.nombreFormateado,
                            modifier = Modifier.size(160.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "¡Atrapaste a ${poke.nombreFormateado}!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        if (!tiposTexto.isNullOrBlank()) {
                            Text(
                                text = "Tipo: $tiposTexto",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Primario
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Completaste ${materia.titulo} - Lección ${leccion?.numero ?: ""}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            pokemonGanado = null
                            navAtras()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primario)
                    ) {
                        Text("¡Genial!", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
fun TarjetaReproductorAudio(
    titulo: String,
    claveLeccion: String = "general",
    rutaPersonalizada: String? = null,
    modifier: Modifier = Modifier
) {
    val audio = rememberReproductorAudio(
        claveLeccion = claveLeccion,
        rutaPersonalizada = rutaPersonalizada
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (audio.reproduciendo) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                    contentDescription = null,
                    tint = Primario
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = titulo,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = if (audio.tieneAudioDocente) "Audio personalizado del docente" else "Audio demostrativo de la lección",
                        fontSize = 12.sp,
                        color = if (audio.tieneAudioDocente) Color(0xFF2E7D32) else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = audio.posicionMs.toFloat(),
                onValueChange = { nuevoValor -> audio.buscarTiempo(nuevoValor.toInt()) },
                valueRange = 0f..(audio.duracionMs.coerceAtLeast(1).toFloat()),
                colors = SliderDefaults.colors(
                    thumbColor = Primario,
                    activeTrackColor = Primario
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatearTiempoMs(audio.posicionMs), fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                Text(formatearTiempoMs(audio.duracionMs), fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { audio.saltarSegundos(-5) },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Primario),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("-5s", color = Primario, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = audio.alternarPlayPause,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (audio.reproduciendo) Color(0xFF2E7D32) else Primario
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = if (audio.reproduciendo) Icons.Default.Refresh else Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (audio.reproduciendo) "Pausar" else "Escuchar",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { audio.saltarSegundos(5) },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Primario),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("+5s", color = Primario, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private fun formatearTiempoMs(milisegundos: Int): String {
    val totalSegundos = (milisegundos / 1000).coerceAtLeast(0)
    val minutos = totalSegundos / 60
    val segundos = totalSegundos % 60
    return "%02d:%02d".format(minutos, segundos)
}