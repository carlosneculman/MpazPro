package cl.duoc.mpazpro.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cl.duoc.mpazpro.data.UsuarioRepositorio
import cl.duoc.mpazpro.domain.Validaciones

class AprendizajeViewModel (private val repositorio: UsuarioRepositorio) : ViewModel(){

    var esCorrecta = mutableStateOf(false)
        private set

    var intentosRestantes = mutableStateOf(3)
        private set

    var mensajeFeedback = mutableStateOf("")
        private set

    // Función que se ejecuta cuando el niño presiona "Opción A" u "Opción B"
    fun verificarRespuesta(opcionElegida: String, respuestaCorrecta: String) {
        // Si ya no le quedan intentos, no hacemos nada
        if (intentosRestantes.value <= 0 && !esCorrecta.value) return

        // Usamos la validación que agregaste en domain/Validaciones.kt
        if (Validaciones.esRespuestaCorrecta(opcionElegida, respuestaCorrecta)) {
            esCorrecta.value = true
            mensajeFeedback.value = "¡Correcto! Respuesta acertada."
        } else {
            intentosRestantes.value -= 1
            esCorrecta.value = false

            if (intentosRestantes.value > 0) {
                mensajeFeedback.value = "Incorrecto. Intenta de nuevo (Quedan ${intentosRestantes.value} intentos)."
            } else {
                mensajeFeedback.value = "Te quedaste sin intentos."
            }
        }
    }

    // Función para el botón azul "Marcar como Completada"
    fun guardarLeccion(materia: String, idLeccion: String) {
        if (esCorrecta.value) {
            repositorio.guardarProgresoLeccion(materia, idLeccion, completada = true)
        }
    }

    // Reinicia los valores cuando entra a una nueva lección
    fun reiniciarLeccion() {
        esCorrecta.value = false
        intentosRestantes.value = 3
        mensajeFeedback.value = ""
    }
}