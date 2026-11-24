package cl.duoc.mpazpro.data.remote

data class MedallaDto(
    val id_medalla: Int? = null,
    val id_user: Int,
    val materia: String,
    val numero_leccion: Int,
    val id_pokemon: Int,
    val nombre_pokemon: String
)

data class DesbloquearMedallaResponse(
    val nueva: Boolean,
    val medalla: MedallaDto? = null,
    val mensaje: String? = null
)