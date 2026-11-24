package cl.duoc.mpazpro.ui.ajustes

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale


@Composable
fun MiMascotaScreen(
    ajustesRepositorio: AjustesRepositorio,
    onVolver: () -> Unit,
    onIrAFiestas: () -> Unit
) {
    val estado by ajustesRepositorio.estado.collectAsState()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var mascotaSeleccionada by remember(estado.mascota) { mutableStateOf(estado.mascota) }
    var nombreMascota by remember(estado.nombreMascota) { mutableStateOf(estado.nombreMascota) }

    fun vibrarSiActivo() {
        if (estado.avisosVibracionYLuz) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    val launcherVoz = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val textoReconocido = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
                ?.trim()
            if (!textoReconocido.isNullOrBlank()) {
                nombreMascota = textoReconocido.replaceFirstChar { it.uppercase() }
            }
        }
    }

    val nombreVisible = nombreMascota.trim().ifBlank { "Bruno" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BarraVolverAjustes(
            titulo = "Mi mascota",
            onVolver = {
                vibrarSiActivo()
                onVolver()
            }
        )


        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = mascotaSeleccionada.emoji, fontSize = 38.sp)
                }
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = "${mascotaSeleccionada.sonido} Soy $nombreVisible, ¡vamos a aprender juntos!",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }


        Text(
            text = "Elige a tu amigo o amiga",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        val mascotas = MascotaTipo.entries
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            mascotas.chunked(3).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    fila.forEach { mascota ->
                        val elegida = mascotaSeleccionada == mascota
                        Card(
                            onClick = {
                                vibrarSiActivo()
                                mascotaSeleccionada = mascota
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (elegida) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            ),
                            border = BorderStroke(
                                width = if (elegida) 2.5.dp else 1.dp,
                                color = if (elegida) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.6f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = mascota.emoji, fontSize = 38.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (elegida) "${mascota.etiqueta} ✓" else mascota.etiqueta,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    repeat(3 - fila.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Text(
            text = "Ponle un nombre",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = nombreMascota,
                onValueChange = { if (it.length <= 20) nombreMascota = it },
                singleLine = true,
                placeholder = { Text("Ej: Bruno") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            Surface(
                onClick = {
                    vibrarSiActivo()
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("es", "CL").toLanguageTag())
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Di el nombre de tu mascota")
                    }
                    runCatching {
                        launcherVoz.launch(intent)
                    }.onFailure {
                        Toast.makeText(
                            context,
                            "Reconocimiento de voz no disponible en este emulador",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.onBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Voz",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
        }

        Button(
            onClick = {
                vibrarSiActivo()
                ajustesRepositorio.guardarMascota(mascotaSeleccionada, nombreVisible)
                onIrAFiestas()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(
                text = "Vestir a $nombreVisible para las fiestas",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                vibrarSiActivo()
                ajustesRepositorio.guardarMascota(mascotaSeleccionada, nombreVisible)
                onVolver()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Guardar mi mascota",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


@Composable
fun FiestasChileScreen(
    ajustesRepositorio: AjustesRepositorio,
    onVolver: () -> Unit,
    onIrAPiezasTraje: () -> Unit
) {
    val estado by ajustesRepositorio.estado.collectAsState()
    val haptic = LocalHapticFeedback.current

    fun vibrarSiActivo() {
        if (estado.avisosVibracionYLuz) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        BarraVolverAjustes(
            titulo = "Fiestas de Chile",
            onVolver = {
                vibrarSiActivo()
                onVolver()
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = estado.mascota.emoji, fontSize = 36.sp)
                    Text(
                        text = estado.fiestaSeleccionada.emoji,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(2.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = estado.fiestaSeleccionada.titulo,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Traje completo: gorro, polera y zapatos.\nToca la fiesta que quieras.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EstiloTraje.entries.forEach { estilo ->
                val activo = estado.estiloTraje == estilo
                Surface(
                    onClick = {
                        vibrarSiActivo()
                        ajustesRepositorio.elegirEstiloTraje(estilo)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = if (activo) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    border = BorderStroke(
                        width = if (activo) 2.dp else 1.dp,
                        color = if (activo) MaterialTheme.colorScheme.primary else Color.LightGray
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (activo) "${estilo.titulo} ✓" else estilo.titulo,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FiestaChile.entries.chunked(3).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    fila.forEach { fiesta ->
                        val elegida = estado.fiestaSeleccionada == fiesta
                        Card(
                            onClick = {
                                vibrarSiActivo()
                                ajustesRepositorio.elegirFiesta(fiesta)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (elegida) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            ),
                            border = BorderStroke(
                                width = if (elegida) 2.5.dp else 1.dp,
                                color = if (elegida) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.6f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = estado.mascota.emoji, fontSize = 32.sp)
                                    Text(
                                        text = fiesta.emoji,
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = 8.dp, y = (-4).dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = fiesta.titulo,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2
                                )
                                Text(
                                    text = fiesta.fecha,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        TarjetaCheckboxAjuste(
            titulo = "Vestirlo solo cuando llegue la fiesta",
            subtitulo = "Cambia solo según el calendario",
            checked = estado.vestirSoloEnFiesta,
            onCheckedChange = {
                vibrarSiActivo()
                ajustesRepositorio.setVestirSoloEnFiesta(it)
            }
        )

        Text(
            text = "Después puedes quitar o cambiar cada pieza.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                vibrarSiActivo()
                ajustesRepositorio.setPiezasTraje(gorro = true, polera = true, zapatos = true)
                onIrAPiezasTraje()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Ponerle el traje completo",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun PiezasTrajeScreen(
    ajustesRepositorio: AjustesRepositorio,
    onVolver: () -> Unit,
    onGuardarYVolver: () -> Unit
) {
    val estado by ajustesRepositorio.estado.collectAsState()
    val haptic = LocalHapticFeedback.current

    var gorro by remember(estado.gorroPuesto) { mutableStateOf(estado.gorroPuesto) }
    var polera by remember(estado.poleraPuesta) { mutableStateOf(estado.poleraPuesta) }
    var zapatos by remember(estado.zapatosPuestos) { mutableStateOf(estado.zapatosPuestos) }

    fun vibrarSiActivo() {
        if (estado.avisosVibracionYLuz) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    val estilo = estado.estiloTraje
    val nombreCortoEstilo = if (estilo == EstiloTraje.HUASO) "Huaso" else "China"
    val tituloTraje = if (estilo == EstiloTraje.HUASO) "Traje de huaso" else "Traje de china o huasa"
    val piezasPuestas = listOf(gorro, polera, zapatos).count { it }

    val subtituloEstado = when (piezasPuestas) {
        3 -> "Traje completo puesto. Quita o deja cada pieza como quieras."
        0 -> "Sin piezas puestas. Toca cualquier prenda para ponérsela."
        else -> "$piezasPuestas de 3 piezas puestas. Quita o deja cada pieza como quieras."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        BarraVolverAjustes(
            titulo = "${estado.fiestaSeleccionada.titulo} · $nombreCortoEstilo",
            onVolver = {
                vibrarSiActivo()
                onVolver()
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = estado.mascota.emoji, fontSize = 36.sp)
                        if (gorro) {
                            Text(
                                text = estilo.emojiGorro,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset(y = (-2).dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (polera) Text(text = estilo.emojiPolera, fontSize = 15.sp)
                        if (zapatos) Text(text = estilo.emojiZapatos, fontSize = 15.sp)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tituloTraje,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = subtituloEstado,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Text(
            text = "Piezas del traje",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        TarjetaPiezaTraje(
            emoji = estilo.emojiGorro,
            titulo = "Gorro",
            descripcion = estilo.nombreGorro,
            puesto = gorro,
            onToggle = {
                vibrarSiActivo()
                gorro = it
            }
        )

        TarjetaPiezaTraje(
            emoji = estilo.emojiPolera,
            titulo = "Polera",
            descripcion = estilo.nombrePolera,
            puesto = polera,
            onToggle = {
                vibrarSiActivo()
                polera = it
            }
        )

        TarjetaPiezaTraje(
            emoji = estilo.emojiZapatos,
            titulo = "Zapatos",
            descripcion = estilo.nombreZapatos,
            puesto = zapatos,
            onToggle = {
                vibrarSiActivo()
                zapatos = it
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    vibrarSiActivo()
                    gorro = false
                    polera = false
                    zapatos = false
                },
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Quitar todo",
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = {
                    vibrarSiActivo()
                    gorro = true
                    polera = true
                    zapatos = true
                },
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Ponerlo todo",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {
                vibrarSiActivo()
                ajustesRepositorio.setPiezasTraje(gorro, polera, zapatos)
                onGuardarYVolver()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Guardar cómo quedó",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


@Composable
private fun BarraVolverAjustes(
    titulo: String,
    onVolver: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            onClick = onVolver,
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Text(
                text = "Volver",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
        Text(
            text = titulo,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )
    }
}

@Composable
private fun TarjetaPiezaTraje(
    emoji: String,
    titulo: String,
    descripcion: String,
    puesto: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!puesto) },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 26.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = descripcion,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Text(
                text = if (puesto) "Puesto" else "Quitado",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (puesto) MaterialTheme.colorScheme.primary else Color.Gray
            )
            Checkbox(
                checked = puesto,
                onCheckedChange = onToggle
            )
        }
    }
}