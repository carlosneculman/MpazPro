package cl.duoc.mpazpro.nativo

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

/**
 * Devuelve una función que abre la cámara del sistema. Al aceptar la foto se llama a
 * [onFoto] con la ruta real del archivo JPG guardado en el almacenamiento privado de la app.
 * (Usa la app de cámara del sistema, así que no hace falta el permiso CAMERA.)
 */
@Composable
fun rememberCamara(onFoto: (String) -> Unit): () -> Unit {
    val context = LocalContext.current
    // rememberSaveable: si el sistema recrea la Activity mientras se abre la cámara, no se pierde la ruta.
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

/**
 * Devuelve una función que pide el permiso de notificaciones (Android 13+) y avisa el resultado
 * con [onResultado]. En versiones anteriores no hay diálogo: solo se consulta si las
 * notificaciones de la app están habilitadas en el sistema.
 */
@Composable
fun rememberPermisoNotificaciones(onResultado: (Boolean) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
        onResultado
    )

    return {
        when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ->
                onResultado(NotificationManagerCompat.from(context).areNotificationsEnabled())

            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED -> onResultado(true)

            else -> launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
