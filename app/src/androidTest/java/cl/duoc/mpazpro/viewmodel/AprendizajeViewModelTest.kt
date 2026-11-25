package cl.duoc.mpazpro.viewmodel

/*import cl.duoc.mpazpro.data.UsuarioRepositorio
import cl.duoc.mpazpro.domain.EstadoRevisionUtp
import cl.duoc.mpazpro.domain.Materia

class AprendizajeViewModelTest : StringSpec({

    "verificarRespuesta debe marcar correcta y guardar progreso en repositorio" {
        val mockRepo = mockk<UsuarioRepositorio>(relaxed = true)
        val viewModel = AprendizajeViewModel(mockRepo)

        runTest {
            viewModel.verificarRespuesta("A", "A")

            viewModel.esCorrecta.value shouldBe true
            viewModel.mensajeFeedback.value shouldBe "¡Correcto! Respuesta acertada."

            viewModel.guardarLeccion("LENGUAJE", "1")
            verify(exactly = 1) {
                mockRepo.guardarProgresoLeccion("LENGUAJE", "1", completada = true)
            }
        }
    }

    "verificarRespuesta incorrecta debe descontar un intento" {
        val mockRepo = mockk<UsuarioRepositorio>(relaxed = true)
        val viewModel = AprendizajeViewModel(mockRepo)

        runTest {
            viewModel.verificarRespuesta("B", "A")

            viewModel.esCorrecta.value shouldBe false
            viewModel.intentosRestantes.value shouldBe 2
        }
    }

    "seleccionarFiltroUtp y aprobarSolicitudUtp deben actualizar utpState" {
        val mockRepo = mockk<UsuarioRepositorio>(relaxed = true)
        val viewModel = AprendizajeViewModel(mockRepo)

        runTest {
            viewModel.seleccionarFiltroUtp(Materia.LENGUAJE)
            viewModel.utpState.value.filtroMateria shouldBe Materia.LENGUAJE
            viewModel.utpState.value.solicitudesFiltradas.shouldNotBeEmpty()

            viewModel.aprobarSolicitudUtp("LENGUAJE_1")
            val aprobada = viewModel.utpState.value.solicitudes.find { it.id == "LENGUAJE_1" }
            aprobada?.estado shouldBe EstadoRevisionUtp.APROBADA
        }
    }
}) */