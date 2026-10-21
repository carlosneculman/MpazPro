package cl.duoc.mpazpro.ui.components

import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.exifinterface.media.ExifInterface
import cl.duoc.mpazpro.domain.Campo
import cl.duoc.mpazpro.ui.theme.*

@Composable
fun ContenidoCentrado(c: @Composable ColumnScope.() -> Unit) {
    Box(Modifier.fillMaxSize().background(Color(0xFFF8F9FA)), Alignment.TopCenter) {
        Column(Modifier.widthIn(max = 520.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp), content = c)
    }
}

@Composable
fun BotonPrincipal(texto: String, onClick: () -> Unit, habilitado: Boolean = true) {
    Button(onClick = onClick, enabled = habilitado, shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(containerColor = Primario), modifier = Modifier.fillMaxWidth().height(52.dp)) {
        Text(texto, fontSize = 16.sp, color = Color.White)
    }
}

@Composable
fun CampoTexto(campo: Campo, onCambio: (String) -> Unit, etiqueta: String, icono: ImageVector, esClave: Boolean = false) {
    var ver by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = campo.valor, onValueChange = onCambio, label = { Text(etiqueta, fontSize = 14.sp) },
        singleLine = true, isError = campo.error != null,
        leadingIcon = { Icon(icono, null, tint = if (campo.error != null) ErrorRojo else if (campo.valido) Exito else Color.Gray) },
        trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (campo.error != null) Icon(Icons.Default.Error, null, tint = ErrorRojo)
                else if (campo.valido && !esClave) Icon(Icons.Default.CheckCircle, null, tint = Exito)
                if (esClave) IconButton(onClick = { ver = !ver }) { Icon(if (ver) Icons.Default.VisibilityOff else Icons.Default.Visibility, null) }
            }
        },
        supportingText = { if (campo.error != null) Text(campo.error!!, color = ErrorRojo) },
        visualTransformation = if (esClave && !ver) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (esClave) KeyboardType.Password else KeyboardType.Text),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primario, unfocusedBorderColor = if (campo.valido) Exito else Color.LightGray, errorBorderColor = ErrorRojo,
            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, errorContainerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun BannerError(texto: String) {
    Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Color(0xFF323232)).padding(16.dp)) {
        Text(texto, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun BarraProgreso(fraccion: Float, color: Color) {
    LinearProgressIndicator(progress = { fraccion }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
        color = color, trackColor = Color.White.copy(alpha = 0.3f), strokeCap = StrokeCap.Round, drawStopIndicator = {})
}

@Composable
fun FotoPerfil(ruta: String?, tam: Dp, conInsigniaCamara: Boolean = false, onClickCamara: () -> Unit = {}) {
    val bit = remember(ruta) { try { BitmapFactory.decodeFile(ruta)?.let {
        b -> val g = when (ExifInterface(ruta!!).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)) {
        6 -> 90f; 3 -> 180f; 8 -> 270f; else -> 0f }; if (g != 0f)

        android.graphics.Bitmap.createBitmap(b, 0, 0, b.width, b.height, Matrix().apply {
            postRotate(g) }, true) else b } } catch (e: Exception) { null }?.asImageBitmap()
    }

    Box(Modifier.size(if (conInsigniaCamara) tam + 12.dp else tam), contentAlignment = Alignment.Center) {
        Box(Modifier.size(tam).clip(CircleShape).background(Color(0xFFE8EAF6)), Alignment.Center) {
            if (bit != null) Image(bit, "", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            else Icon(Icons.Default.Person, "", Modifier.size(tam * 0.6f), tint = Primario)
        }

        if (conInsigniaCamara) {
            Box(Modifier.align(Alignment.BottomEnd).offset(x = (-4).dp, y = (-4).dp).size(32.dp).clip(CircleShape).background(Primario).clickable { onClickCamara() }, Alignment.Center) {
                Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}