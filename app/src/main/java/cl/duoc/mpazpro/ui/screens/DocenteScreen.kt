package cl.duoc.mpazpro.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.mpazpro.domain.*
import cl.duoc.mpazpro.nativo.obtenerRutaAudioGuardado
import cl.duoc.mpazpro.nativo.rememberSelectorAudio
import cl.duoc.mpazpro.ui.components.*
import cl.duoc.mpazpro.ui.theme.Primario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocenteScreen(
    navPerfil: () -> Unit
) {
    var materiaSeleccionada by rememberSaveable { mutableStateOf(Materia.LENGUAJE) }
    var numeroLeccionEditando by rememberSaveable { mutableStateOf<Int?>(null) }
    var creandoNuevaLeccion by rememberSaveable { mutableStateOf(false) }

    var mostrarInforme by rememberSaveable { mutableStateOf(false) }
    var informeValidado by rememberSaveable { mutableStateOf(false) }
    var mensajeDescarga by rememberSaveable { mutableStateOf(false) }

    var listaLecciones by remember(materiaSeleccionada) {
        mutableStateOf(Contenido.lecciones(materiaSeleccionada))
    }

    val pendientesUtpMateria = Contenido.obtenerSolicitudesUtp().count {
        it.materia == materiaSeleccionada && it.estado == EstadoRevisionUtp.PENDIENTE
    }

    if (numeroLeccionEditando != null || creandoNuevaLeccion) {
        val siguienteNumero = (listaLecciones.maxOfOrNull { it.numero } ?: 0) + 1
        val leccionBase = if (creandoNuevaLeccion) {
            Leccion(
                numero = siguienteNumero,
                titulo = "",
                estado = if (listaLecciones.isEmpty()) EstadoLeccion.EN_CURSO else EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = Contenido.objetivoPorMateria(materiaSeleccionada),
                descripcion = "Módulo de aprendizaje interactivo con apoyo de audio.",
                contenido = "Escribe aquí las instrucciones y el contenido pedagógico de la lección.",
                pistaMascota = "¡Tú puedes! Escucha el audio y lee con calma.",
                iconoVisual = "📘",
                pregunta = "¿Cuál es la respuesta correcta para completar esta lección?",
                opcionA = "Opción A",
                opcionB = "Opción B",
                respuestaCorrecta = "A"
            )
        } else {
            listaLecciones.find { it.numero == numeroLeccionEditando }
        }

        if (leccionBase != null) {
            EditorLeccionDocenteView(
                materia = materiaSeleccionada,
                leccionInicial = leccionBase,
                esNueva = creandoNuevaLeccion,
                onVolver = {
                    numeroLeccionEditando = null
                    creandoNuevaLeccion = false
                },
                onGuardar = { leccionActualizada ->
                    Contenido.guardarLeccionDocente(materiaSeleccionada, leccionActualizada)
                    listaLecciones = Contenido.lecciones(materiaSeleccionada).toList()
                    numeroLeccionEditando = null
                    creandoNuevaLeccion = false
                }
            )
            return
        }
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
                onClick = { creandoNuevaLeccion = true },
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
                colors = CardDefaults.cardColors(containerColor = materiaSeleccionada.colores().principal),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Gestión de Contenidos y Evaluaciones", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Asignatura: ${materiaSeleccionada.titulo} (3° Básico)", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Lecciones: ${listaLecciones.size}", color = Color.White, fontSize = 12.sp)
                        Text("En revisión UTP: $pendientesUtpMateria", color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Selecciona una asignatura:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Materia.entries.forEach { materia ->
                    val seleccionada = materia == materiaSeleccionada
                    val colorMateria = materia.colores().principal

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { materiaSeleccionada = materia },
                        color = if (seleccionada) colorMateria else Color.White,
                        border = BorderStroke(1.dp, if (seleccionada) colorMateria else Color.LightGray),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = materia.icono(),
                                contentDescription = materia.titulo,
                                tint = if (seleccionada) Color.White else colorMateria,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = materia.titulo,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (seleccionada) Color.White else Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listaLecciones) { leccion ->
                    val colorMateria = materiaSeleccionada.colores().principal
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { numeroLeccionEditando = leccion.numero },
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
                                    .background(colorMateria.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(leccion.numero.toString(), fontWeight = FontWeight.Bold, color = colorMateria)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${leccion.iconoVisual}${leccion.titulo}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(leccion.descripcion, fontSize = 12.sp, color = Color.Gray)
                                Text(
                                    text = "Pregunta: ${leccion.pregunta} (Correcta:${leccion.respuestaCorrecta})",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                leccion.objetivoRelacionado?.let {
                                    Text(
                                        text = "OA: ${it.codigoOficial} -${it.ejeCurricular}",
                                        fontSize = 10.sp,
                                        color = colorMateria,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            IconButton(onClick = { numeroLeccionEditando = leccion.numero }) {
                                Icon(Icons.Default.Edit, contentDescription = "Personalizar lección y audio", tint = colorMateria)
                            }
                        }
                    }
                }
            }
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
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Rendimiento del Curso (Tercero Básico A)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("• Asignatura seleccionada: ${materiaSeleccionada.titulo}", fontSize = 13.sp)
                        Text("• Total de lecciones cargadas: ${listaLecciones.size}", fontSize = 13.sp)
                        Text("• Fortalezas: Participación activa en módulos con apoyo de audio y mascota.", fontSize = 13.sp, color = Color(0xFF2E7D32))
                        Text("• Aspectos a Reforzar: Completar las etapas pendientes de la unidad.", fontSize = 13.sp, color = Color(0xFFC62828))

                        HorizontalDivider(color = Color.LightGray)

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
                            Text("Informe guardado en Descargas (PDF)", fontSize = 12.sp, color = Primario, fontWeight = FontWeight.Bold)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorLeccionDocenteView(
    materia: Materia,
    leccionInicial: Leccion,
    esNueva: Boolean,
    onVolver: () -> Unit,
    onGuardar: (Leccion) -> Unit
) {
    val context = LocalContext.current
    val claveAudio = "${materia.name}_${leccionInicial.numero}"

    var titulo by rememberSaveable(leccionInicial.numero, esNueva) { mutableStateOf(leccionInicial.titulo) }
    var descripcion by rememberSaveable(leccionInicial.numero, esNueva) { mutableStateOf(leccionInicial.descripcion) }
    var contenido by rememberSaveable(leccionInicial.numero, esNueva) { mutableStateOf(leccionInicial.contenido) }
    var iconoVisual by rememberSaveable(leccionInicial.numero, esNueva) { mutableStateOf(leccionInicial.iconoVisual) }
    var pistaMascota by rememberSaveable(leccionInicial.numero, esNueva) { mutableStateOf(leccionInicial.pistaMascota) }
    var pregunta by rememberSaveable(leccionInicial.numero, esNueva) { mutableStateOf(leccionInicial.pregunta) }
    var opcionA by rememberSaveable(leccionInicial.numero, esNueva) { mutableStateOf(leccionInicial.opcionA) }
    var opcionB by rememberSaveable(leccionInicial.numero, esNueva) { mutableStateOf(leccionInicial.opcionB) }
    var respuestaCorrecta by rememberSaveable(leccionInicial.numero, esNueva) { mutableStateOf(leccionInicial.respuestaCorrecta) }
    var errorTitulo by rememberSaveable(leccionInicial.numero, esNueva) { mutableStateOf(false) }

    var rutaAudioSubido by rememberSaveable(leccionInicial.numero, esNueva) {
        mutableStateOf(obtenerRutaAudioGuardado(context, claveAudio))
    }

    val abrirSelectorAudio = rememberSelectorAudio(claveLeccion = claveAudio) { nuevaRuta ->
        rutaAudioSubido = nuevaRuta
    }

    val scrollState = rememberScrollState()
    val colorMateria = materia.colores().principal

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (esNueva) "Nueva Lección (${materia.titulo})" else "Personalizar Lección ${leccionInicial.numero}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorMateria)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Text(
                text = "Vista Docente — ${materia.titulo}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorMateria
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    if (it.isNotBlank()) errorTitulo = false
                },
                label = { Text("Título de la lección") },
                isError = errorTitulo,
                supportingText = {
                    if (errorTitulo) Text("El título es obligatorio", color = Color(0xFFC62828))
                },
                trailingIcon = {
                    if (errorTitulo) Icon(Icons.Default.Error, contentDescription = "Error", tint = Color(0xFFC62828))
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción breve") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = iconoVisual,
                onValueChange = { iconoVisual = it },
                label = { Text("Ícono visual (emojis de apoyo)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Audio de apoyo para el estudiante",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Selecciona cualquier audio de tu dispositivo. El reproductor tomará su duración automáticamente.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = abrirSelectorAudio,
                        colors = ButtonDefaults.buttonColors(containerColor = colorMateria),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AudioFile, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (rutaAudioSubido != null) "Cambiar audio desde el dispositivo" else "Subir audio desde el dispositivo",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TarjetaReproductorAudio(
                        titulo = titulo.ifBlank { "Audio de la lección ${leccionInicial.numero}" },
                        claveLeccion = claveAudio,
                        rutaPersonalizada = rutaAudioSubido
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contenido,
                onValueChange = { contenido = it },
                label = { Text("Contenido e instrucciones para el alumno") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = pistaMascota,
                onValueChange = { pistaMascota = it },
                label = { Text("Pista de la mascota") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Actividad de comprobación (A / B)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = pregunta,
                        onValueChange = { pregunta = it },
                        label = { Text("Pregunta para el estudiante") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = opcionA,
                        onValueChange = { opcionA = it },
                        label = { Text("Opción A") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = opcionB,
                        onValueChange = { opcionB = it },
                        label = { Text("Opción B") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Respuesta correcta:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val esCorrectaA = respuestaCorrecta.equals("A", ignoreCase = true)
                        Button(
                            onClick = { respuestaCorrecta = "A" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (esCorrectaA) Color(0xFF2E7D32) else Color.White,
                                contentColor = if (esCorrectaA) Color.White else Color.Black
                            ),
                            border = BorderStroke(1.dp, if (esCorrectaA) Color(0xFF2E7D32) else Color.LightGray)
                        ) {
                            Text("Correcta: A", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { respuestaCorrecta = "B" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!esCorrectaA) Color(0xFF2E7D32) else Color.White,
                                contentColor = if (!esCorrectaA) Color.White else Color.Black
                            ),
                            border = BorderStroke(1.dp, if (!esCorrectaA) Color(0xFF2E7D32) else Color.LightGray)
                        ) {
                            Text("Correcta: B", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (titulo.isBlank()) {
                        errorTitulo = true
                    } else {
                        onGuardar(
                            leccionInicial.copy(
                                titulo = titulo.trim(),
                                descripcion = descripcion.ifBlank { "Módulo de aprendizaje interactivo con apoyo de audio." },
                                contenido = contenido.ifBlank { "Sigue las instrucciones del audio y responde la pregunta." },
                                iconoVisual = iconoVisual.ifBlank { "📘" },
                                pistaMascota = pistaMascota.ifBlank { "¡Tú puedes! Escucha el audio y lee con calma." },
                                pregunta = pregunta.ifBlank { "¿Cuál es la respuesta correcta para completar esta lección?" },
                                opcionA = opcionA.ifBlank { "Opción A" },
                                opcionB = opcionB.ifBlank { "Opción B" },
                                respuestaCorrecta = if (respuestaCorrecta.equals("B", ignoreCase = true)) "B" else "A"
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primario),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar cambios en la lección", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}