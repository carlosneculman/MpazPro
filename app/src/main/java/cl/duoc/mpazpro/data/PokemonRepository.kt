package cl.duoc.mpazpro.data

import android.content.Context
import cl.duoc.mpazpro.data.remote.PokeApiClient
import cl.duoc.mpazpro.model.OfficialArtwork
import cl.duoc.mpazpro.model.OtherSprites
import cl.duoc.mpazpro.model.Pokemon
import cl.duoc.mpazpro.model.Sprites

class PokemonRepository(context: Context? = null) {
    private val api = PokeApiClient.getApi(context)

    suspend fun obtenerPokemonAleatorio(): Pokemon {
        val idAleatorio = (1..1025).random()
        return try {
            val response = api.getPokemon(idAleatorio.toString())
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                pokemonRespaldo(idAleatorio)
            }
        } catch (_: Exception) {
            pokemonRespaldo(idAleatorio)
        }
    }

    private fun pokemonRespaldo(id: Int): Pokemon {
        return Pokemon(
            id = id,
            name = "pokemon-$id",
            sprites = Sprites(
                other = OtherSprites(
                    officialArtwork = OfficialArtwork(
                        frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
                    )
                )
            )
        )
    }
}