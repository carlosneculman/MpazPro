package cl.duoc.mpazpro.data.remote

import android.content.Context
import cl.duoc.mpazpro.model.Pokemon
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.File

interface PokeApiService {
    @GET("pokemon/{idOrName}")
    suspend fun getPokemon(
        @Path("idOrName") idOrName: String
    ): Response<Pokemon>
}

object PokeApiClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"
    private const val CACHE_SIZE_BYTES = 10L * 1024 * 1024

    private var instancia: PokeApiService? = null

    fun getApi(context: Context? = null): PokeApiService {
        instancia?.let { return it }

        val clientBuilder = OkHttpClient.Builder()
        if (context != null) {
            val cacheDir = File(context.cacheDir, "pokeapi_http")
            clientBuilder.cache(Cache(cacheDir, CACHE_SIZE_BYTES))
        }

        val service = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)

        instancia = service
        return service
    }
}