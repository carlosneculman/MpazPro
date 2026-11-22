package cl.duoc.mpazpro.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


data class LoginRequest(val email: String, val contrasena: String)
data class UsuarioLogin(val id_user: String, val nombre: String, val rol: Int)
data class LoginResponse(val mensaje: String, val token: String, val usuario: UsuarioLogin)
data class RegistroRequest(
    val email: String, val nombre_user: String, val contrasena: String,
    val rut_user: String?, val telefono_user: String?, val id_rol: Int
)

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body b: LoginRequest): Response<LoginResponse>
    @POST("api/auth/registro")
    suspend fun registro(@Body b: RegistroRequest): Response<ResponseBody>
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/"
    val api: ApiService by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }
}