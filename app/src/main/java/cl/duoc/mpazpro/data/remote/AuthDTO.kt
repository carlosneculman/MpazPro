package cl.duoc.mpazpro.data.remote

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("contrasena") val contrasena: String
)

data class LoginResponse(
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("token") val token: String,
    @SerializedName("usuario") val usuario: UsuarioDto
)

data class UsuarioDto(
    @SerializedName("id_user") val id_user: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("rol") val rol: Int
)

data class RegistroRequest(
    @SerializedName("email") val email: String,
    @SerializedName("nombre_user") val nombre_user: String,
    @SerializedName("contrasena") val contrasena: String,
    @SerializedName("rut_user") val rut_user: String?,
    @SerializedName("telefono_user") val telefono_user: String?,
    @SerializedName("id_rol") val id_rol: Int
)

data class UserImgRequest(
    val id_user: Int,
    val imagen_base64: String,
    val mime_type: String = "image/jpeg"
)

data class UserImgResponse(
    val id_user: Int,
    val imagen_base64: String,
    val mime_type: String?
)