package cl.duoc.mpazpro

import cl.duoc.mpazpro.domain.Validaciones
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacionesTest {

    @Test
    fun correo_aceptaFormatosValidos() {
        assertNull(Validaciones.correo("ana@mpaz.cl"))
        assertNull(Validaciones.correo("  ana.perez+curso@colegio.edu.cl  "))
    }

    @Test
    fun correo_rechazaVacioYFormatosInvalidos() {
        assertNotNull(Validaciones.correo(""))
        assertNotNull(Validaciones.correo("   "))
        assertNotNull(Validaciones.correo("ana"))
        assertNotNull(Validaciones.correo("ana@"))
        assertNotNull(Validaciones.correo("ana@mpaz"))
        assertNotNull(Validaciones.correo("@mpaz.cl"))
    }

    @Test
    fun clave_exigeLargoMinimo() {
        assertNotNull(Validaciones.clave(""))
        assertNotNull(Validaciones.clave("12345"))
        assertNull(Validaciones.clave("123456"))
    }

    @Test
    fun repetir_debeCoincidir() {
        assertNotNull(Validaciones.repetir("abcdef", ""))
        assertNotNull(Validaciones.repetir("abcdef", "abcdeg"))
        assertNull(Validaciones.repetir("abcdef", "abcdef"))
    }

    @Test
    fun nombre_validaLargoYNumeros() {
        assertNotNull(Validaciones.nombre(""))
        assertNotNull(Validaciones.nombre("A"))
        assertNotNull(Validaciones.nombre("Ana2"))
        assertNull(Validaciones.nombre("María José"))
    }

    @Test
    fun nombre_mensajeUsaLaEtiqueta() {
        assertEquals("Ingresa tu apellido", Validaciones.nombre("", "apellido"))
    }

    @Test
    fun campoValidado_noMuestraErrorSiEstaVacio() {
        val vacio = campoValidado("", "Ingresa tu correo")
        assertNull(vacio.error)
        assertFalse(vacio.valido)
    }

    @Test
    fun campoValidado_marcaErrorOValido() {
        val malo = campoValidado("ana", "Correo no válido")
        assertEquals("Correo no válido", malo.error)
        assertFalse(malo.valido)

        val bueno = campoValidado("ana@mpaz.cl", null)
        assertNull(bueno.error)
        assertTrue(bueno.valido)
    }

    @Test
    fun revisado_muestraErrorAunqueEsteVacio() {
        val campo = Campo().revisado("Ingresa tu correo")
        assertEquals("Ingresa tu correo", campo.error)
        assertFalse(campo.valido)
    }
}
