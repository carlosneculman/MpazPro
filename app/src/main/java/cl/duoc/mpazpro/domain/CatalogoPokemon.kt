package cl.duoc.mpazpro.domain

data class RecompensaPokemon(
    val idPokemon: Int,
    val nombre: String
) {
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$idPokemon.png"
}

object CatalogoPokemon {
    fun obtenerPorLeccion(materia: Materia, numeroLeccion: Int): RecompensaPokemon {
        return when (materia) {
            Materia.LENGUAJE -> when (numeroLeccion) {
                1 -> RecompensaPokemon(1, "Bulbasaur")
                2 -> RecompensaPokemon(2, "Ivysaur")
                else -> RecompensaPokemon(3, "Venusaur")
            }
            Materia.MATEMATICAS -> when (numeroLeccion) {
                1 -> RecompensaPokemon(4, "Charmander")
                2 -> RecompensaPokemon(5, "Charmeleon")
                else -> RecompensaPokemon(6, "Charizard")
            }
            Materia.CIENCIAS -> when (numeroLeccion) {
                1 -> RecompensaPokemon(7, "Squirtle")
                2 -> RecompensaPokemon(8, "Wartortle")
                else -> RecompensaPokemon(9, "Blastoise")
            }
            Materia.HISTORIA -> when (numeroLeccion) {
                1 -> RecompensaPokemon(25, "Pikachu")
                2 -> RecompensaPokemon(26, "Raichu")
                else -> RecompensaPokemon(133, "Eevee")
            }
        }
    }
}