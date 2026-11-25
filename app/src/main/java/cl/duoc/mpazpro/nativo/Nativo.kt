package cl.duoc.mpazpro.nativo

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import cl.duoc.mpazpro.R
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sin

private const val CANAL_RECORDATORIOS_ID = "canal_recordatorios_mpaz"
private const val NOTIFICACION_RECORDATORIO_ID = 1001

fun rutaImagenABase64(rutaArchivo: String): String? {
    return try {
        val archivo = File(rutaArchivo)
        if (!archivo.exists()) return null
        val original = BitmapFactory.decodeFile(archivo.absolutePath) ?: return null
        val escalada = Bitmap.createScaledBitmap(original, 400, 400, true)
        val salida = ByteArrayOutputStream()
        escalada.compress(Bitmap.CompressFormat.JPEG, 75, salida)
        Base64.encodeToString(salida.toByteArray(), Base64.NO_WRAP)
    } catch (e: Exception) {
        null
    }
}

fun base64ABytes(base64: String?): ByteArray? {
    if (base64.isNullOrBlank()) return null
    return try {
        Base64.decode(base64, Base64.DEFAULT)
    } catch (e: Exception) {
        null
    }
}

@Composable
fun rememberCamara(onFoto: (String) -> Unit): () -> Unit {
    val context = LocalContext.current
    var pendiente by rememberSaveable { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { tomada ->
        val ruta = pendiente
        pendiente = null
        if (ruta != null) {
            if (tomada) onFoto(ruta) else File(ruta).delete()
        }
    }

    return {
        try {
            val carpeta = File(context.filesDir, "fotos").apply { mkdirs() }
            val archivo = File(carpeta, "perfil_${System.currentTimeMillis()}.jpg")
            pendiente = archivo.absolutePath
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
            launcher.launch(uri)
        } catch (e: ActivityNotFoundException) {
            pendiente = null
            Toast.makeText(context, "No se encontró una aplicación de cámara", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun rememberGaleria(onFoto: (String) -> Unit): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            try {
                val carpeta = File(context.filesDir, "fotos").apply { mkdirs() }
                val archivo = File(carpeta, "galeria_${System.currentTimeMillis()}.jpg")

                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(archivo).use { output ->
                        input.copyTo(output)
                    }
                }

                if (archivo.exists() && archivo.length() > 0L) {
                    onFoto(archivo.absolutePath)
                } else {
                    archivo.delete()
                    Toast.makeText(context, "No se pudo cargar la imagen seleccionada", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error al importar la imagen de la galería", Toast.LENGTH_SHORT).show()
            }
        }
    }

    return {
        try {
            launcher.launch("image/*")
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No se encontró una aplicación de galería", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun rememberSelectorAudio(
    claveLeccion: String,
    onAudioSeleccionado: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            try {
                val carpeta = File(context.filesDir, "audios").apply { mkdirs() }
                carpeta.listFiles()
                    ?.filter { it.name.startsWith("audio_${claveLeccion}_") }
                    ?.forEach { it.delete() }

                val archivo = File(carpeta, "audio_${claveLeccion}_${System.currentTimeMillis()}.mp3")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(archivo).use { output ->
                        input.copyTo(output)
                    }
                }

                if (archivo.exists() && archivo.length() > 0L) {
                    onAudioSeleccionado(archivo.absolutePath)
                    Toast.makeText(context, "Audio guardado en la lección", Toast.LENGTH_SHORT).show()
                } else {
                    archivo.delete()
                    Toast.makeText(context, "No se pudo cargar el archivo de audio", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error al importar el archivo de audio", Toast.LENGTH_SHORT).show()
            }
        }
    }

    return {
        try {
            launcher.launch("audio/*")
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No se encontró un selector de archivos de audio", Toast.LENGTH_SHORT).show()
        }
    }
}

fun obtenerRutaAudioGuardado(context: Context, claveLeccion: String): String? {
    val carpeta = File(context.filesDir, "audios")
    if (!carpeta.exists()) return null
    return carpeta.listFiles()
        ?.firstOrNull { it.name.startsWith("audio_${claveLeccion}_") && it.length() > 0L }
        ?.absolutePath
}

@Composable
fun rememberPermisoNotificaciones(onResultado: (Boolean) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        onResultado(concedido)
        if (concedido) {
            mostrarNotificacionRecordatorio(context)
        } else {
            Toast.makeText(context, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
        }
    }

    return {
        when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> {
                val habilitadas = NotificationManagerCompat.from(context).areNotificationsEnabled()
                onResultado(habilitadas)
                if (habilitadas) {
                    mostrarNotificacionRecordatorio(context)
                }
            }

            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED -> {
                onResultado(true)
                mostrarNotificacionRecordatorio(context)
            }

            else -> launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

fun mostrarNotificacionRecordatorio(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val canal = NotificationChannel(
            CANAL_RECORDATORIOS_ID,
            "Recordatorios de Estudio",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Avisos para repasar las lecciones en Escuela Escritora Marcela Paz"
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(canal)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    val notificacion = NotificationCompat.Builder(context, CANAL_RECORDATORIOS_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Escuela Escritora Marcela Paz")
        .setContentText("¡Recordatorio activado! Es un buen momento para avanzar en tus lecciones.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(NOTIFICACION_RECORDATORIO_ID, notificacion)
    Toast.makeText(context, "Recordatorio de estudio activado", Toast.LENGTH_SHORT).show()
}

fun cancelarNotificacionRecordatorio(context: Context) {
    NotificationManagerCompat.from(context).cancel(NOTIFICACION_RECORDATORIO_ID)
    Toast.makeText(context, "Recordatorio de estudio desactivado", Toast.LENGTH_SHORT).show()
}

class ControladorAudio(
    val reproduciendo: Boolean,
    val posicionMs: Int,
    val duracionMs: Int,
    val tieneAudioDocente: Boolean,
    val alternarPlayPause: () -> Unit,
    val buscarTiempo: (Int) -> Unit,
    val saltarSegundos: (Int) -> Unit
)

@Composable
fun rememberReproductorAudio(
    claveLeccion: String = "general",
    rutaPersonalizada: String? = null
): ControladorAudio {
    val context = LocalContext.current
    var reproduciendo by remember { mutableStateOf(false) }
    var posicionMs by remember { mutableIntStateOf(0) }
    var duracionMs by remember { mutableIntStateOf(0) }

    val rutaAudioDocente = remember(claveLeccion, rutaPersonalizada) {
        rutaPersonalizada ?: obtenerRutaAudioGuardado(context, claveLeccion)
    }

    val mediaPlayer = remember(claveLeccion, rutaAudioDocente) {
        try {
            reproduciendo = false
            posicionMs = 0

            val mp = when {
                rutaAudioDocente != null && File(rutaAudioDocente).exists() -> {
                    MediaPlayer.create(context, Uri.fromFile(File(rutaAudioDocente)))
                }
                else -> {
                    val resId = context.resources.getIdentifier("audio_leccion", "raw", context.packageName)
                    if (resId != 0) {
                        MediaPlayer.create(context, resId)
                    } else {
                        val archivoDemo = obtenerAudioLocalDemo(context)
                        MediaPlayer.create(context, Uri.fromFile(archivoDemo))
                    }
                }
            }

            mp?.apply {
                duracionMs = duration.coerceAtLeast(1)
                setOnCompletionListener {
                    reproduciendo = false
                    posicionMs = 0
                    seekTo(0)
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    LaunchedEffect(reproduciendo, mediaPlayer) {
        while (reproduciendo && mediaPlayer != null) {
            if (mediaPlayer.isPlaying) {
                posicionMs = mediaPlayer.currentPosition
            }
            delay(200L)
        }
    }

    DisposableEffect(mediaPlayer) {
        onDispose {
            try {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer.stop()
                }
                mediaPlayer?.release()
            } catch (_: Exception) {
            }
        }
    }

    return ControladorAudio(
        reproduciendo = reproduciendo,
        posicionMs = posicionMs,
        duracionMs = duracionMs,
        tieneAudioDocente = rutaAudioDocente != null,
        alternarPlayPause = {
            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    mp.pause()
                    reproduciendo = false
                } else {
                    mp.start()
                    reproduciendo = true
                }
            }
        },
        buscarTiempo = { nuevoTiempoMs ->
            mediaPlayer?.let { mp ->
                val limite = duracionMs.coerceAtLeast(1)
                val tiempoSeguro = nuevoTiempoMs.coerceIn(0, limite)
                mp.seekTo(tiempoSeguro)
                posicionMs = tiempoSeguro
            }
        },
        saltarSegundos = { segundos ->
            mediaPlayer?.let { mp ->
                val limite = duracionMs.coerceAtLeast(1)
                val nuevoTiempo = (mp.currentPosition + (segundos * 1000)).coerceIn(0, limite)
                mp.seekTo(nuevoTiempo)
                posicionMs = nuevoTiempo
            }
        }
    )
}

private fun obtenerAudioLocalDemo(context: Context): File {
    val archivo = File(context.cacheDir, "audio_leccion_demo.wav")
    if (archivo.exists() && archivo.length() > 1000L) return archivo

    val sampleRate = 8000
    val duracionSegundos = 30
    val numSamples = sampleRate * duracionSegundos
    val dataSize = numSamples * 2
    val notas = doubleArrayOf(261.63, 329.63, 392.00, 523.25, 392.00, 329.63)

    FileOutputStream(archivo).use { out ->
        val header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN)
        header.put("RIFF".toByteArray())
        header.putInt(36 + dataSize)
        header.put("WAVE".toByteArray())
        header.put("fmt ".toByteArray())
        header.putInt(16)
        header.putShort(1)
        header.putShort(1)
        header.putInt(sampleRate)
        header.putInt(sampleRate * 2)
        header.putShort(2)
        header.putShort(16)
        header.put("data".toByteArray())
        header.putInt(dataSize)
        out.write(header.array())

        val buffer = ByteBuffer.allocate(sampleRate * 2).order(ByteOrder.LITTLE_ENDIAN)
        for (segundo in 0 until duracionSegundos) {
            buffer.clear()
            val freq = notas[segundo % notas.size]
            for (i in 0 until sampleRate) {
                val envolvente = 1.0 - (i.toDouble() / sampleRate) * 0.7
                val muestra = (sin(2.0 * Math.PI * i * freq / sampleRate) * 9000.0 * envolvente).toInt().toShort()
                buffer.putShort(muestra)
            }
            out.write(buffer.array())
        }
    }
    return archivo
}