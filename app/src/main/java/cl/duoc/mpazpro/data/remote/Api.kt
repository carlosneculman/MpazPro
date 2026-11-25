package cl.duoc.mpazpro.data.remote

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body b: LoginRequest): Response<LoginResponse>

    @POST("api/auth/registro")
    suspend fun registro(@Body b: RegistroRequest): Response<ResponseBody>

    @GET("api/medallas/{id_user}")
    suspend fun obtenerMedallas(@Path("id_user") idUser: Int): Response<List<MedallaDto>>

    @POST("api/medallas/desbloquear")
    suspend fun desbloquearMedalla(@Body request: MedallaDto): Response<DesbloquearMedallaResponse>

    @POST("api/user-img")
    suspend fun guardarFotoPerfil(@Body request: UserImgRequest): Response<Unit>

    @GET("api/user-img/{id_user}")
    suspend fun obtenerFotoPerfil(@Path("id_user") idUser: Int): Response<UserImgResponse>
}

object RetrofitClient {
    private const val BASE_URL = "http://44.218.174.107:3000/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
