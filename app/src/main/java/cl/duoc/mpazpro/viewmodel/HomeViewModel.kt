package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.data.UsuarioRepositorio
import cl.duoc.mpazpro.domain.Materia
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val nombre: String = "Estudiante",
    val completadas: Map<Materia, Int> = emptyMap()
)

class HomeViewModel(repo: UsuarioRepositorio) : ViewModel() {
    val ui: StateFlow<HomeUiState> = repo.usuario
        .map { HomeUiState(nombre = it?.nombre ?: "Estudiante") }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            HomeUiState(nombre = repo.usuario.value?.nombre ?: "Estudiante")
        )
}
