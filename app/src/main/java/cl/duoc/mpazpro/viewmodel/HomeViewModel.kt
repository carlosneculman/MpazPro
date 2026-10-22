package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.mpazpro.domain.Materia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val nombre: String = "Estudiante",
    val completadas: Map<Materia, Int> = emptyMap()
)

class HomeViewModel : ViewModel() {
    private val _ui = MutableStateFlow(HomeUiState())
    val ui: StateFlow<HomeUiState> = _ui.asStateFlow()
}