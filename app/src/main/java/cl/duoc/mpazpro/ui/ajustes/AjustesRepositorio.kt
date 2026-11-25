package cl.duoc.mpazpro.ui.ajustes

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class MascotaTipo(
    val etiqueta: String,
    val articulo: String,
    val emoji: String,
    val sonido: String
) {
    GATO("Gato", "el gato", "🐱", "¡Miau!"),
    PERRO("Perro", "el perro", "🐶", "¡Guau!"),
    HAMSTER("Hámster", "el hámster", "🐹", "¡Hola!"),
    CUY("Cuy", "el cuy", "🦫", "¡Cui cui!"),
    LORO("Loro", "el loro", "🦜", "¡Hola amigo!")
}

enum class FiestaChile(
    val titulo: String,
    val fecha: String,
    val emoji: String
) {
    FIESTAS_PATRIAS("Fiestas Patrias", "18 y 19 sept.", "🇨🇱"),
    LA_TIRANA("La Tirana", "julio", "🎭"),
    TAPATI_RAPA_NUI("Tapati Rapa Nui", "febrero", "🌺"),
    WE_TRIPANTU("We Tripantu", "junio", "🌅"),
    CUASIMODO("Cuasimodo", "tras Semana Santa", "👒"),
    CHILOE("Chiloé", "enero y febrero", "⛵")
}

enum class EstiloTraje(
    val titulo: String,
    val nombreGorro: String,
    val nombrePolera: String,
    val nombreZapatos: String,
    val emojiGorro: String,
    val emojiPolera: String,
    val emojiZapatos: String
) {
    HUASO(
        titulo = "Huaso",
        nombreGorro = "Chupalla de paja",
        nombrePolera = "Camisa blanca con faja",
        nombreZapatos = "Botas",
        emojiGorro = "👒",
        emojiPolera = "👕",
        emojiZapatos = "👢"
    ),
    CHINA_HUASA(
        titulo = "China / Huasa",
        nombreGorro = "Flores en el pelo",
        nombrePolera = "Vestido floreado con delantal",
        nombreZapatos = "Zapatos de tacón bajo",
        emojiGorro = "🌺",
        emojiPolera = "👗",
        emojiZapatos = "👠"
    )
}

enum class TemaPantalla(val titulo: String) {
    NORMAL("Normal"),
    OSCURO("Oscuro"),
    CONTRASTE("Contraste"),
    INVERTIDO("Invertido")
}

enum class FiltroColor(val titulo: String) {
    SIN_FILTRO("Sin filtro"),
    ROJO_VERDE("Rojo-verde"),
    AZUL_AMARILLO("Azul-amarillo"),
    GRISES("Grises")
}

data class AjustesEstado(
    val mascota: MascotaTipo = MascotaTipo.PERRO,
    val nombreMascota: String = "Bruno",
    val fiestaSeleccionada: FiestaChile = FiestaChile.FIESTAS_PATRIAS,
    val estiloTraje: EstiloTraje = EstiloTraje.HUASO,
    val vestirSoloEnFiesta: Boolean = true,
    val gorroPuesto: Boolean = true,
    val poleraPuesta: Boolean = true,
    val zapatosPuestos: Boolean = true,
    val lecturaVozAlta: Boolean = true,
    val comandosVoz: Boolean = true,
    val recordatorioEstudio: Boolean = false,
    val escalaTexto: Float = 1.0f,
    val tema: TemaPantalla = TemaPantalla.NORMAL,
    val filtroColor: FiltroColor = FiltroColor.SIN_FILTRO,
    val subtitulosYSenas: Boolean = true,
    val avisosVibracionYLuz: Boolean = true
) {
    val descripcionMascota: String
        get() = "${nombreMascota.ifBlank { "Bruno" }} ${mascota.articulo}"

    val etiquetaTamanoLetra: String
        get() = when {
            escalaTexto < 0.95f -> "Letra chica"
            escalaTexto > 1.12f -> "Letra grande"
            else -> "Letra mediana"
        }

    val resumenModoInclusivo: String
        get() = "$etiquetaTamanoLetra · Tema ${tema.titulo.lowercase()}"
}



class AjustesRepositorio(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("mpazpro_ajustes_v2", Context.MODE_PRIVATE)

    private val _estado = MutableStateFlow(cargarEstado())
    val estado: StateFlow<AjustesEstado> = _estado.asStateFlow()

    private fun cargarEstado(): AjustesEstado {
        return AjustesEstado(
            mascota = runCatching {
                MascotaTipo.valueOf(prefs.getString(KEY_MASCOTA, MascotaTipo.PERRO.name)!!)
            }.getOrDefault(MascotaTipo.PERRO),
            nombreMascota = prefs.getString(KEY_NOMBRE_MASCOTA, "Bruno") ?: "Bruno",
            fiestaSeleccionada = runCatching {
                FiestaChile.valueOf(prefs.getString(KEY_FIESTA, FiestaChile.FIESTAS_PATRIAS.name)!!)
            }.getOrDefault(FiestaChile.FIESTAS_PATRIAS),
            estiloTraje = runCatching {
                EstiloTraje.valueOf(prefs.getString(KEY_ESTILO_TRAJE, EstiloTraje.HUASO.name)!!)
            }.getOrDefault(EstiloTraje.HUASO),
            vestirSoloEnFiesta = prefs.getBoolean(KEY_SOLO_EN_FIESTA, true),
            gorroPuesto = prefs.getBoolean(KEY_GORRO, true),
            poleraPuesta = prefs.getBoolean(KEY_POLERA, true),
            zapatosPuestos = prefs.getBoolean(KEY_ZAPATOS, true),
            lecturaVozAlta = prefs.getBoolean(KEY_TTS, true),
            comandosVoz = prefs.getBoolean(KEY_VOZ, true),
            recordatorioEstudio = prefs.getBoolean(KEY_RECORDATORIO, false),
            escalaTexto = prefs.getFloat(KEY_ESCALA_TEXTO, 1.0f),
            tema = runCatching {
                TemaPantalla.valueOf(prefs.getString(KEY_TEMA, TemaPantalla.NORMAL.name)!!)
            }.getOrDefault(TemaPantalla.NORMAL),
            filtroColor = runCatching {
                FiltroColor.valueOf(prefs.getString(KEY_FILTRO_COLOR, FiltroColor.SIN_FILTRO.name)!!)
            }.getOrDefault(FiltroColor.SIN_FILTRO),
            subtitulosYSenas = prefs.getBoolean(KEY_SUBTITULOS, true),
            avisosVibracionYLuz = prefs.getBoolean(KEY_VIBRACION_LUZ, true)
        )
    }

    private fun actualizar(bloque: (AjustesEstado) -> AjustesEstado) {
        val nuevo = bloque(_estado.value)
        _estado.value = nuevo
        prefs.edit()
            .putString(KEY_MASCOTA, nuevo.mascota.name)
            .putString(KEY_NOMBRE_MASCOTA, nuevo.nombreMascota)
            .putString(KEY_FIESTA, nuevo.fiestaSeleccionada.name)
            .putString(KEY_ESTILO_TRAJE, nuevo.estiloTraje.name)
            .putBoolean(KEY_SOLO_EN_FIESTA, nuevo.vestirSoloEnFiesta)
            .putBoolean(KEY_GORRO, nuevo.gorroPuesto)
            .putBoolean(KEY_POLERA, nuevo.poleraPuesta)
            .putBoolean(KEY_ZAPATOS, nuevo.zapatosPuestos)
            .putBoolean(KEY_TTS, nuevo.lecturaVozAlta)
            .putBoolean(KEY_VOZ, nuevo.comandosVoz)
            .putBoolean(KEY_RECORDATORIO, nuevo.recordatorioEstudio)
            .putFloat(KEY_ESCALA_TEXTO, nuevo.escalaTexto)
            .putString(KEY_TEMA, nuevo.tema.name)
            .putString(KEY_FILTRO_COLOR, nuevo.filtroColor.name)
            .putBoolean(KEY_SUBTITULOS, nuevo.subtitulosYSenas)
            .putBoolean(KEY_VIBRACION_LUZ, nuevo.avisosVibracionYLuz)
            .apply()
    }

    fun guardarMascota(tipo: MascotaTipo, nombre: String) = actualizar {
        it.copy(mascota = tipo, nombreMascota = nombre.trim().ifBlank { "Bruno" })
    }
    fun elegirFiesta(fiesta: FiestaChile) = actualizar { it.copy(fiestaSeleccionada = fiesta) }
    fun elegirEstiloTraje(estilo: EstiloTraje) = actualizar { it.copy(estiloTraje = estilo) }
    fun setVestirSoloEnFiesta(activo: Boolean) = actualizar { it.copy(vestirSoloEnFiesta = activo) }
    fun setPiezasTraje(gorro: Boolean, polera: Boolean, zapatos: Boolean) = actualizar {
        it.copy(gorroPuesto = gorro, poleraPuesta = polera, zapatosPuestos = zapatos)
    }
    fun setLecturaVozAlta(activo: Boolean) = actualizar { it.copy(lecturaVozAlta = activo) }
    fun setComandosVoz(activo: Boolean) = actualizar { it.copy(comandosVoz = activo) }
    fun setRecordatorioEstudio(activo: Boolean) = actualizar { it.copy(recordatorioEstudio = activo) }
    fun setEscalaTexto(escala: Float) = actualizar { it.copy(escalaTexto = escala.coerceIn(0.85f, 1.35f)) }
    fun elegirTema(tema: TemaPantalla) = actualizar { it.copy(tema = tema) }
    fun elegirFiltroColor(filtro: FiltroColor) = actualizar { it.copy(filtroColor = filtro) }
    fun setSubtitulosYSenas(activo: Boolean) = actualizar { it.copy(subtitulosYSenas = activo) }
    fun setAvisosVibracionYLuz(activo: Boolean) = actualizar { it.copy(avisosVibracionYLuz = activo) }
    fun aplicarAtajoLetrasChicas() = actualizar { it.copy(escalaTexto = 1.30f) }
    fun aplicarAtajoMolestaLuz() = actualizar { it.copy(tema = TemaPantalla.OSCURO) }
    fun aplicarAtajoConfundoColores() = actualizar { it.copy(filtroColor = FiltroColor.ROJO_VERDE) }
    fun aplicarAtajoPrefieroEscuchar() = actualizar {
        it.copy(lecturaVozAlta = true, comandosVoz = true, subtitulosYSenas = true)
    }

    private companion object {
        const val KEY_MASCOTA = "mascota"
        const val KEY_NOMBRE_MASCOTA = "nombre_mascota"
        const val KEY_FIESTA = "fiesta_chile"
        const val KEY_ESTILO_TRAJE = "estilo_traje"
        const val KEY_SOLO_EN_FIESTA = "solo_en_fiesta"
        const val KEY_GORRO = "pieza_gorro"
        const val KEY_POLERA = "pieza_polera"
        const val KEY_ZAPATOS = "pieza_zapatos"
        const val KEY_TTS = "lectura_voz_alta"
        const val KEY_VOZ = "comandos_voz"
        const val KEY_RECORDATORIO = "recordatorio_estudio"
        const val KEY_ESCALA_TEXTO = "escala_texto"
        const val KEY_TEMA = "tema_pantalla"
        const val KEY_FILTRO_COLOR = "filtro_color"
        const val KEY_SUBTITULOS = "subtitulos_senas"
        const val KEY_VIBRACION_LUZ = "vibracion_luz"
    }
}

fun generarColorSchemeInclusivo(estado: AjustesEstado): ColorScheme {
    when (estado.filtroColor) {
        FiltroColor.GRISES -> {
            return if (estado.tema == TemaPantalla.OSCURO || estado.tema == TemaPantalla.CONTRASTE) {
                darkColorScheme(
                    primary = Color(0xFFD1D5DB),
                    onPrimary = Color.Black,
                    secondary = Color(0xFF9CA3AF),
                    background = Color(0xFF18181B),
                    onBackground = Color(0xFFF4F4F5),
                    surface = Color(0xFF27272A),
                    onSurface = Color(0xFFF4F4F5)
                )
            } else {
                lightColorScheme(
                    primary = Color(0xFF3F3F46),
                    onPrimary = Color.White,
                    secondary = Color(0xFF71717A),
                    background = Color(0xFFF4F4F5),
                    onBackground = Color(0xFF18181B),
                    surface = Color.White,
                    onSurface = Color(0xFF18181B)
                )
            }
        }
        FiltroColor.ROJO_VERDE -> {
            return if (estado.tema == TemaPantalla.OSCURO) {
                darkColorScheme(
                    primary = Color(0xFF56B4E9),
                    onPrimary = Color.Black,
                    secondary = Color(0xFFE69F00),
                    background = Color(0xFF121821),
                    onBackground = Color.White,
                    surface = Color(0xFF1C2533),
                    onSurface = Color.White
                )
            } else {
                lightColorScheme(
                    primary = Color(0xFF0072B2),
                    onPrimary = Color.White,
                    secondary = Color(0xFFE69F00),
                    background = Color(0xFFF7FAFC),
                    onBackground = Color(0xFF0F172A),
                    surface = Color.White,
                    onSurface = Color(0xFF0F172A)
                )
            }
        }
        FiltroColor.AZUL_AMARILLO -> {
            return lightColorScheme(
                primary = Color(0xFFD81B60),
                onPrimary = Color.White,
                secondary = Color(0xFF00897B),
                background = Color(0xFFFAF8F5),
                onBackground = Color(0xFF1A1A1A),
                surface = Color.White,
                onSurface = Color(0xFF1A1A1A)
            )
        }
        FiltroColor.SIN_FILTRO -> Unit
    }
    return when (estado.tema) {
        TemaPantalla.NORMAL -> lightColorScheme(
            primary = Color(0xFF2554D7),
            onPrimary = Color.White,
            secondary = Color(0xFFFCC737),
            onSecondary = Color(0xFF1E1E1E),
            background = Color(0xFFFFF8E8),
            onBackground = Color(0xFF1B1E28),
            surface = Color.White,
            onSurface = Color(0xFF1B1E28),
            surfaceVariant = Color(0xFFFFF1CF),
            onSurfaceVariant = Color(0xFF3E3A2F)
        )
        TemaPantalla.OSCURO -> darkColorScheme(
            primary = Color(0xFF6B8EFF),
            onPrimary = Color(0xFF0B1220),
            secondary = Color(0xFFFCC737),
            onSecondary = Color.Black,
            background = Color(0xFF1E222B),
            onBackground = Color(0xFFF3F4F6),
            surface = Color(0xFF2A2F3B),
            onSurface = Color(0xFFF3F4F6),
            surfaceVariant = Color(0xFF343A48),
            onSurfaceVariant = Color(0xFFD1D5DB)
        )
        TemaPantalla.CONTRASTE -> darkColorScheme(
            primary = Color(0xFFFFEA00),
            onPrimary = Color.Black,
            secondary = Color.White,
            onSecondary = Color.Black,
            background = Color.Black,
            onBackground = Color(0xFFFFEA00),
            surface = Color(0xFF121212),
            onSurface = Color.White,
            surfaceVariant = Color(0xFF1F1F1F),
            onSurfaceVariant = Color(0xFFFFEA00)
        )
        TemaPantalla.INVERTIDO -> darkColorScheme(
            primary = Color(0xFF7DD3FC),
            onPrimary = Color(0xFF082F49),
            secondary = Color(0xFFFDE047),
            onSecondary = Color.Black,
            background = Color(0xFF0C2336),
            onBackground = Color(0xFFE0F2FE),
            surface = Color(0xFF14324A),
            onSurface = Color(0xFFE0F2FE),
            surfaceVariant = Color(0xFF1D4361),
            onSurfaceVariant = Color(0xFFBAE6FD)
        )
    }
}