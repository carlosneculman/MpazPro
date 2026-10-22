package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.mpazpro.MpazApplication
import cl.duoc.mpazpro.data.UsuarioRepositorio

/**
 * Fábrica única de ViewModels. Todo ViewModel que una pantalla pida con
 * `viewModel(factory = AppViewModelProvider.Factory)` DEBE tener su `initializer` aquí;
 * si falta, la app se cae con "Cannot find initializer for ...".
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer { HomeViewModel(repositorio()) }
        initializer { LoginViewModel(repositorio()) }
        initializer { RegistroViewModel(repositorio()) }
        initializer { RecuperarViewModel(repositorio()) }
        initializer { PerfilViewModel(repositorio()) }
    }
}

private fun CreationExtras.repositorio(): UsuarioRepositorio =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MpazApplication).repositorio
