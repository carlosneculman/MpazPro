package cl.duoc.mpazpro.ui.ajustes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AjustesPrincipalScreen(
    ajustesRepositorio: AjustesRepositorio,
    onIrAMascota: () -> Unit,
    onIrAModoInclusivo: () -> Unit
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
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Ajustes",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        TarjetaResumenAjuste(
            icono = {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = estado.mascota.emoji, fontSize = 30.sp)
                }
            },
            etiqueta = "Mi mascota",
            titulo = estado.descripcionMascota,
            subtitulo = "Traje: ${estado.fiestaSeleccionada.titulo}",
            onCambiar = {
                vibrarSiActivo()
                onIrAMascota()
            }
        )

        TarjetaResumenAjuste(
            icono = {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aa",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            },
            etiqueta = "Modo inclusivo",
            titulo = estado.resumenModoInclusivo,
            subtitulo = if (estado.filtroColor != FiltroColor.SIN_FILTRO) {
                "Filtro: ${estado.filtroColor.titulo}"
            } else {
                null
            },
            onCambiar = {
                vibrarSiActivo()
                onIrAModoInclusivo()
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Ayudas rápidas",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        TarjetaCheckboxAjuste(
            titulo = "Lectura en voz alta",
            subtitulo = "Tu mascota lee todo por ti",
            checked = estado.lecturaVozAlta,
            onCheckedChange = {
                vibrarSiActivo()
                ajustesRepositorio.setLecturaVozAlta(it)
            }
        )

        TarjetaCheckboxAjuste(
            titulo = "Comandos de voz",
            subtitulo = "Di «siguiente» o «repetir»",
            checked = estado.comandosVoz,
            onCheckedChange = {
                vibrarSiActivo()
                ajustesRepositorio.setComandosVoz(it)
            }
        )

        TarjetaCheckboxAjuste(
            titulo = "Recordatorio de estudio",
            subtitulo = "Todos los días a las 4:30 p. m.",
            checked = estado.recordatorioEstudio,
            onCheckedChange = {
                vibrarSiActivo()
                ajustesRepositorio.setRecordatorioEstudio(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun ModoInclusivoScreen(
    ajustesRepositorio: AjustesRepositorio,
    onVolver: () -> Unit
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                onClick = {
                    vibrarSiActivo()
                    onVolver()
                },
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
                text = "Modo inclusivo",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            text = "Toca lo que se parece a ti",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TarjetaAtajoInclusivo(
                titulo = "Veo las letras chicas",
                subtitulo = "Letra grande y más espacio",
                activo = estado.escalaTexto >= 1.25f,
                modifier = Modifier.weight(1f),
                onClick = {
                    vibrarSiActivo()
                    ajustesRepositorio.aplicarAtajoLetrasChicas()
                }
            )
            TarjetaAtajoInclusivo(
                titulo = "Me molesta la luz",
                subtitulo = "Fondo oscuro y suave",
                activo = estado.tema == TemaPantalla.OSCURO,
                modifier = Modifier.weight(1f),
                onClick = {
                    vibrarSiActivo()
                    ajustesRepositorio.aplicarAtajoMolestaLuz()
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TarjetaAtajoInclusivo(
                titulo = "Confundo colores",
                subtitulo = "Íconos y colores seguros",
                activo = estado.filtroColor != FiltroColor.SIN_FILTRO,
                modifier = Modifier.weight(1f),
                onClick = {
                    vibrarSiActivo()
                    ajustesRepositorio.aplicarAtajoConfundoColores()
                }
            )
            TarjetaAtajoInclusivo(
                titulo = "Prefiero escuchar",
                subtitulo = "Voz y descripciones",
                activo = estado.lecturaVozAlta && estado.comandosVoz,
                modifier = Modifier.weight(1f),
                onClick = {
                    vibrarSiActivo()
                    ajustesRepositorio.aplicarAtajoPrefieroEscuchar()
                }
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Tamaño de letra",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "a",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Slider(
                        value = estado.escalaTexto,
                        onValueChange = { ajustesRepositorio.setEscalaTexto(it) },
                        valueRange = 0.85f..1.35f,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "A",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "Hola, soy ${estado.nombreMascota}.",
                    fontSize = (18 * estado.escalaTexto).sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Text(
            text = "Colores de la pantalla",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TarjetaTemaPreview(
                tema = TemaPantalla.NORMAL,
                fondo = Color.White,
                texto = Color(0xFF1B1E28),
                seleccionado = estado.tema == TemaPantalla.NORMAL,
                modifier = Modifier.weight(1f),
                onClick = {
                    vibrarSiActivo()
                    ajustesRepositorio.elegirTema(TemaPantalla.NORMAL)
                }
            )
            TarjetaTemaPreview(
                tema = TemaPantalla.OSCURO,
                fondo = Color(0xFF2B303A),
                texto = Color.White,
                seleccionado = estado.tema == TemaPantalla.OSCURO,
                modifier = Modifier.weight(1f),
                onClick = {
                    vibrarSiActivo()
                    ajustesRepositorio.elegirTema(TemaPantalla.OSCURO)
                }
            )
            TarjetaTemaPreview(
                tema = TemaPantalla.CONTRASTE,
                fondo = Color.Black,
                texto = Color(0xFFFFEA00),
                seleccionado = estado.tema == TemaPantalla.CONTRASTE,
                modifier = Modifier.weight(1f),
                onClick = {
                    vibrarSiActivo()
                    ajustesRepositorio.elegirTema(TemaPantalla.CONTRASTE)
                }
            )
            TarjetaTemaPreview(
                tema = TemaPantalla.INVERTIDO,
                fondo = Color(0xFF0E2A42),
                texto = Color(0xFFE0F2FE),
                seleccionado = estado.tema == TemaPantalla.INVERTIDO,
                modifier = Modifier.weight(1f),
                onClick = {
                    vibrarSiActivo()
                    ajustesRepositorio.elegirTema(TemaPantalla.INVERTIDO)
                }
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    FiltroColor.SIN_FILTRO,
                    FiltroColor.ROJO_VERDE,
                    FiltroColor.AZUL_AMARILLO
                ).forEach { filtro ->
                    ChipFiltroColor(
                        filtro = filtro,
                        seleccionado = estado.filtroColor == filtro,
                        onClick = {
                            vibrarSiActivo()
                            ajustesRepositorio.elegirFiltroColor(filtro)
                        }
                    )
                }
            }
            ChipFiltroColor(
                filtro = FiltroColor.GRISES,
                seleccionado = estado.filtroColor == FiltroColor.GRISES,
                onClick = {
                    vibrarSiActivo()
                    ajustesRepositorio.elegirFiltroColor(FiltroColor.GRISES)
                }
            )
        }
        TarjetaCheckboxAjuste(
            titulo = "Subtítulos y lengua de señas",
            subtitulo = "En cada audio",
            checked = estado.subtitulosYSenas,
            onCheckedChange = {
                vibrarSiActivo()
                ajustesRepositorio.setSubtitulosYSenas(it)
            }
        )

        TarjetaCheckboxAjuste(
            titulo = "Avisos con vibración y luz",
            subtitulo = "Sin depender del sonido",
            checked = estado.avisosVibracionYLuz,
            onCheckedChange = {
                ajustesRepositorio.setAvisosVibracionYLuz(it)
                if (it) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TarjetaResumenAjuste(
    icono: @Composable () -> Unit,
    etiqueta: String,
    titulo: String,
    subtitulo: String?,
    onCambiar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icono()
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = etiqueta,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
                Text(
                    text = titulo,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitulo != null) {
                    Text(
                        text = subtitulo,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                onClick = onCambiar,
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
            ) {
                Text(
                    text = "Cambiar",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
internal fun TarjetaCheckboxAjuste(
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitulo,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
private fun TarjetaAtajoInclusivo(
    titulo: String,
    subtitulo: String,
    activo: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.heightIn(min = 92.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = if (activo) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitulo,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun TarjetaTemaPreview(
    tema: TemaPantalla,
    fondo: Color,
    texto: Color,
    seleccionado: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(74.dp),
        shape = RoundedCornerShape(14.dp),
        color = fondo,
        border = BorderStroke(
            width = if (seleccionado) 3.dp else 1.dp,
            color = if (seleccionado) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Aa",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = texto
            )
            Text(
                text = if (seleccionado) "${tema.titulo} ✓" else tema.titulo,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = texto,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ChipFiltroColor(
    filtro: FiltroColor,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (seleccionado) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = if (seleccionado) "${filtro.titulo} ✓" else filtro.titulo,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}