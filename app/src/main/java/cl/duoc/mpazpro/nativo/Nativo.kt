package cl.duoc.mpazpro.nativo

import androidx.compose.runtime.Composable

@Composable
fun rememberCamara(onFoto: (String) -> Unit): () -> Unit {
    return { onFoto("ruta_de_imagen_temporal") }
}

@Composable
fun rememberPermisoNotificaciones(onResultado: (Boolean) -> Unit): () -> Unit {
    return { onResultado(true) }
}