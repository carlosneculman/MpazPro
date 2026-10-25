package cl.duoc.mpazpro

import android.app.Application
import cl.duoc.mpazpro.data.UsuarioRepositorio

class MpazApplication : Application() {
    val repositorio: UsuarioRepositorio by lazy { UsuarioRepositorio(this) }
}
