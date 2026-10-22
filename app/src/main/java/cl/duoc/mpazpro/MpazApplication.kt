package cl.duoc.mpazpro

import android.app.Application
import cl.duoc.mpazpro.data.UsuarioRepositorio

class MpazApplication : Application() {
    /** Única instancia compartida por todas las pantallas/ViewModels. */
    val repositorio: UsuarioRepositorio by lazy { UsuarioRepositorio(this) }
}
