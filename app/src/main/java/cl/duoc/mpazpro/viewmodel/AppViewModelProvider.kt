package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel()
        }
    }
}