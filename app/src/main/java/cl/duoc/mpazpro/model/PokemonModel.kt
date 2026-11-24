package cl.duoc.mpazpro.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("sprites") val sprites: Sprites? = Sprites(),
    @SerializedName("types") val types: List<Types>? = null
) {
    val nombreFormateado: String
        get() = name?.replaceFirstChar { it.uppercase() } ?: "Desconocido"

    val imageUrl: String?
        get() = sprites?.other?.officialArtwork?.frontDefault ?: sprites?.frontDefault
}

data class Sprites(
    @SerializedName("front_default") val frontDefault: String? = null,
    @SerializedName("other") val other: OtherSprites? = null
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork? = null
)

data class OfficialArtwork(
    @SerializedName("front_default") val frontDefault: String? = null
)

data class Types(
    @SerializedName("slot") val slot: Int? = null,
    @SerializedName("type") val type: NamedResource? = null
)

data class NamedResource(
    @SerializedName("name") val name: String? = null,
    @SerializedName("url") val url: String? = null
)