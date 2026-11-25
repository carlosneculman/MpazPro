package cl.duoc.mpazpro.domain

enum class RolUsuario { UTP, DOCENTE, ESTUDIANTE }

data class Usuario(
    val id: String,
    val nombre: String,
    val apellido: String = "",
    val correo: String = "",
    val rol: RolUsuario,
    val curso: String? = null,
    val fotoPath: String? = null,
    val recordatorios: Boolean = false
)

data class ObjetivoAprendizaje(
    val idOa: String,
    val codigoOficial: String,
    val asignatura: String,
    val unidad: Int = 1,
    val ejeCurricular: String,
    val descripcion: String,
    val estadoAprobacion: Boolean = false
)

enum class EstadoLeccion { COMPLETADA, EN_CURSO, BLOQUEADA }

enum class EstadoRevisionUtp { PENDIENTE, APROBADA, CAMBIOS_SOLICITADOS }

data class SolicitudUtp(
    val id: String,
    val materia: Materia,
    val numeroLeccion: Int,
    val titulo: String,
    val docente: String,
    val descripcion: String,
    val estado: EstadoRevisionUtp = EstadoRevisionUtp.PENDIENTE
) {
    val claveAudio: String get() = "${materia.name}_$numeroLeccion"
}

data class Leccion(
    val numero: Int,
    val titulo: String,
    var estado: EstadoLeccion,
    val objetivoRelacionado: ObjetivoAprendizaje? = null,
    val descripcion: String = "Módulo de aprendizaje interactivo con apoyo de audio.",
    val contenido: String = "Aquí irá el texto pedagógico y las instrucciones del docente.",
    val audioResId: Int? = null,
    val pistaMascota: String = "¡Tú puedes! Escucha el audio y lee con calma.",
    val iconoVisual: String = "📘",
    val pregunta: String = "¿Cuál es la respuesta correcta para completar esta lección?",
    val opcionA: String = "Opción A",
    val opcionB: String = "Opción B",
    val respuestaCorrecta: String = "A"
)

data class ResultadoEvaluacion(
    val estudianteId: String,
    val leccionNumero: Int,
    val intentosUsados: Int,
    val intentosMaximos: Int = 3,
    val puntajeObtenido: Int,
    val aprobada: Boolean,
    val respuestasCorrectas: List<Int>,
    val respuestasIncorrectas: List<Int>
)

object Contenido {
    private val oasEjemplo = mapOf(
        Materia.LENGUAJE to ObjetivoAprendizaje("OA1_L", "OA01", "Lenguaje", 1, "Lectura", "Leer de manera fluida textos breves."),
        Materia.MATEMATICAS to ObjetivoAprendizaje("OA1_M", "OA01", "Matemáticas", 1, "Números", "Demostrar que comprenden la adición y sustracción."),
        Materia.HISTORIA to ObjetivoAprendizaje("OA1_H", "OA01", "Historia", 1, "Sociedad", "Reconocer símbolos patrios y su significado."),
        Materia.CIENCIAS to ObjetivoAprendizaje("OA1_C", "OA01", "Ciencias", 1, "Biología", "Identificar los órganos de los sentidos.")
    )

    private val mapaLecciones: MutableMap<Materia, MutableList<Leccion>> = mutableMapOf(
        Materia.LENGUAJE to mutableListOf(
            Leccion(
                numero = 1,
                titulo = "Las vocales",
                estado = EstadoLeccion.EN_CURSO,
                objetivoRelacionado = oasEjemplo[Materia.LENGUAJE],
                descripcion = "Reconoce las cinco vocales al inicio de palabras cotidianas.",
                contenido = "Las vocales son A, E, I, O, U. Nos ayudan a formar todas las palabras que leemos y escuchamos cada día.",
                pistaMascota = "¡Hola! Pronuncia lento A-vión para oír con qué letra empieza ✈️",
                iconoVisual = "✈️ 🅰️",
                pregunta = "¿Con qué vocal empieza la palabra Avión?",
                opcionA = "Con la letra A",
                opcionB = "Con la letra O",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 2,
                titulo = "Las sílabas",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.LENGUAJE],
                descripcion = "Separa palabras simples en sílabas usando aplausos.",
                contenido = "Cada golpe de voz en una palabra es una sílaba. Por ejemplo: ma-no tiene 2 sílabas y pe-lo-ta tiene 3.",
                pistaMascota = "¡Aplaude conmigo! CA - SA... ¿cuántos aplausos dimos? 👏",
                iconoVisual = "🏠 👏",
                pregunta = "¿Cuántas sílabas tiene la palabra CASA?",
                opcionA = "Tiene 4 sílabas",
                opcionB = "Tiene 2 sílabas (ca-sa)",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 3,
                titulo = "Palabras simples",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.LENGUAJE],
                descripcion = "Identifica palabras de uso diario y su significado.",
                contenido = "Cuando juntamos sílabas formamos palabras que nombran personas, animales y objetos de nuestro entorno.",
                pistaMascota = "¡Mira el dibujo! Es una fruta roja y crujiente 🍎",
                iconoVisual = "🍎 📝",
                pregunta = "¿Qué palabra nombra una fruta?",
                opcionA = "Manzana",
                opcionB = "Cuaderno",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 4,
                titulo = "Oraciones cortas",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.LENGUAJE],
                descripcion = "Arma y comprende oraciones con mayúscula y punto final.",
                contenido = "Una oración expresa una idea completa. Siempre comienza con letra mayúscula y termina con un punto.",
                pistaMascota = "¡Ojo con las pistas! Busca la frase que empieza con mayúscula y tiene punto ✏️",
                iconoVisual = "🐶 📖",
                pregunta = "¿Cuál oración está escrita correctamente?",
                opcionA = "el perro corre mucho",
                opcionB = "El perro corre en el parque.",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 5,
                titulo = "Lectura corta",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.LENGUAJE],
                descripcion = "Lee un microcuento y responde quién es el personaje.",
                contenido = "Texto: 'Matías llevó su cometa azul al cerro. Cuando sopló el viento, la cometa voló muy alto sobre los árboles.'",
                pistaMascota = "¡Vuelve a leer el comienzo! Ahí dice qué objeto llevó Matías 🪁",
                iconoVisual = "🪁 🌤️",
                pregunta = "Según la lectura, ¿qué llevó Matías al cerro?",
                opcionA = "Su cometa azul",
                opcionB = "Una bicicleta verde",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 6,
                titulo = "Sustantivos y acciones",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.LENGUAJE],
                descripcion = "Distingue entre nombres (sustantivos) y acciones (verbos).",
                contenido = "Los verbos nos indican qué acción realiza alguien, como saltar, leer, dibujar o cantar.",
                pistaMascota = "¡Piensa en algo que puedes hacer con tu cuerpo! 🏃",
                iconoVisual = "🏃‍♂️ 📚",
                pregunta = "¿Cuál de estas palabras indica una acción (verbo)?",
                opcionA = "Mochila",
                opcionB = "Saltar",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 7,
                titulo = "Sinónimos y antónimos",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.LENGUAJE],
                descripcion = "Reconoce palabras con significado parecido y opuesto.",
                contenido = "Los sinónimos significan casi lo mismo (feliz y alegre). Los antónimos significan lo contrario (frío y caliente).",
                pistaMascota = "¡Sonrisa gigante! Si estás feliz, también estás... 😊",
                iconoVisual = "😊 ✨",
                pregunta = "¿Cuál es un sinónimo de la palabra FELIZ?",
                opcionA = "Alegre",
                opcionB = "Triste",
                respuestaCorrecta = "A"
            )
        ),
        Materia.MATEMATICAS to mutableListOf(
            Leccion(
                numero = 1,
                titulo = "Números hasta 100",
                estado = EstadoLeccion.EN_CURSO,
                objetivoRelacionado = oasEjemplo[Materia.MATEMATICAS],
                descripcion = "Cuenta elementos y reconoce el orden de los números.",
                contenido = "Contar nos ayuda a saber cuántos objetos hay. Cada número sigue un orden: 1, 2, 3, 4, 5... hasta 100 y más.",
                pistaMascota = "¡Contemos juntos despacito! 🎈 Uno, dos, tres...",
                iconoVisual = "🎈🎈🎈",
                pregunta = "¿Cuántos globos hay en la imagen?",
                opcionA = "Hay 3 globos",
                opcionB = "Hay 5 globos",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 2,
                titulo = "Sumas simples",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.MATEMATICAS],
                descripcion = "Resuelve adiciones cotidianas juntando cantidades.",
                contenido = "Sumar significa juntar o agregar elementos. Si tienes 4 lápices y te regalan 3, ahora tienes 7 en total.",
                pistaMascota = "¡Usa tus dedos! Pon 4 en una mano y 3 en la otra ✋",
                iconoVisual = "✏️➕✏️",
                pregunta = "¿Cuánto es 4 + 3?",
                opcionA = "6",
                opcionB = "7",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 3,
                titulo = "Restas simples",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.MATEMATICAS],
                descripcion = "Resuelve sustracciones quitando elementos de un grupo.",
                contenido = "Restar significa quitar o descubrir cuánto queda. Si había 9 galletas y comimos 4, quedan 5 galletas.",
                pistaMascota = "Tenías 9 galletas 🍪 y te comiste 4. Cuenta cuántas quedan.",
                iconoVisual = "🍪➖4️⃣",
                pregunta = "Si tienes 9 galletas y comes 4, ¿cuántas quedan?",
                opcionA = "5 galletas",
                opcionB = "6 galletas",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 4,
                titulo = "Figuras geométricas",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.MATEMATICAS],
                descripcion = "Identifica triángulos, cuadrados y círculos por sus lados.",
                contenido = "El triángulo tiene 3 lados, el cuadrado tiene 4 lados iguales y el círculo tiene borde curvo sin esquinas.",
                pistaMascota = "¡Tri-ángulo suena a TRES! Cuenta sus puntitas 🔺",
                iconoVisual = "🔺🟦⚪",
                pregunta = "¿Cuántos lados tiene un triángulo?",
                opcionA = "4 lados",
                opcionB = "3 lados",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 5,
                titulo = "Medir y comparar",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.MATEMATICAS],
                descripcion = "Compara longitudes usando centímetros y metros.",
                contenido = "Para medir cosas pequeñas como un lápiz usamos centímetros (cm). Para cosas largas como una sala usamos metros (m).",
                pistaMascota = "¡Piensa en tu regla del estuche! Sirve para cosas cortitas 📏",
                iconoVisual = "📏 ✏️",
                pregunta = "¿Qué usamos normalmente para medir el largo de un lápiz?",
                opcionA = "Centímetros (regla)",
                opcionB = "Kilómetros",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 6,
                titulo = "Tablas del 2 y del 5",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.MATEMATICAS],
                descripcion = "Multiplica sumando grupos iguales de 2 y de 5.",
                contenido = "Multiplicar es una suma rápida. Por ejemplo, 2 x 4 es lo mismo que sumar 2 + 2 + 2 + 2 = 8.",
                pistaMascota = "¡Suma de 2 en 2 cuatro veces: 2, 4, 6... ya casi llegas! ✌️",
                iconoVisual = "✖️ 🔢",
                pregunta = "¿Cuánto es 2 x 4?",
                opcionA = "6",
                opcionB = "8",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 7,
                titulo = "Problemas cotidianos",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.MATEMATICAS],
                descripcion = "Resuelve un desafío matemático de la vida diaria.",
                contenido = "En la mochila de Sofía hay 5 cuadernos y 4 libros. Para saber cuántos objetos hay en total debemos sumar.",
                pistaMascota = "¡Junta los 5 cuadernos con los 4 libros! 🎒",
                iconoVisual = "🎒 📚",
                pregunta = "Sofía tiene 5 cuadernos y 4 libros. ¿Cuántos tiene en total?",
                opcionA = "9 en total",
                opcionB = "1 en total",
                respuestaCorrecta = "A"
            )
        ),
        Materia.HISTORIA to mutableListOf(
            Leccion(
                numero = 1,
                titulo = "Mi familia",
                estado = EstadoLeccion.EN_CURSO,
                objetivoRelacionado = oasEjemplo[Materia.HISTORIA],
                descripcion = "Reconoce la importancia del cariño y respeto en la familia.",
                contenido = "Todas las familias son distintas y especiales. En familia nos cuidamos, compartimos costumbres y nos ayudamos.",
                pistaMascota = "¡Qué bonito! Piensa en cómo nos ayudamos en casa ❤️",
                iconoVisual = "👨‍👩‍👧‍👦 ❤️",
                pregunta = "¿Qué valor ayuda a convivir bien en familia?",
                opcionA = "El respeto y la ayuda mutua",
                opcionB = "No escuchar a nadie",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 2,
                titulo = "Mi escuela",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.HISTORIA],
                descripcion = "Conoce las normas de convivencia dentro de la escuela.",
                contenido = "En la escuela aprendemos junto a compañeros y profesores. Escuchar con respeto y cuidar la sala hace feliz a todos.",
                pistaMascota = "¡Igual que en la Escuela Marcela Paz! Piensa en una buena norma 🏫",
                iconoVisual = "🏫 🤝",
                pregunta = "¿Cuál es una buena norma de convivencia en clases?",
                opcionA = "Interrumpir cuando alguien habla",
                opcionB = "Levantar la mano para opinar",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 3,
                titulo = "Mi comunidad",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.HISTORIA],
                descripcion = "Identifica servicios e instituciones importantes del barrio.",
                contenido = "En nuestra comunidad trabajan personas que nos cuidan y ayudan, como bomberos, doctores, profesores y recolectores.",
                pistaMascota = "¡Suena la sirena roja! Ellos apagan incendios y rescatan personas 🚒",
                iconoVisual = "🚒 🏘️",
                pregunta = "¿Quiénes ayudan a apagar incendios en la comunidad?",
                opcionA = "Los Bomberos",
                opcionB = "Los panaderos",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 4,
                titulo = "Símbolos patrios",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.HISTORIA],
                descripcion = "Identifica la bandera, el escudo y el copihue de Chile.",
                contenido = "La bandera de Chile tiene los colores blanco, azul y rojo, y una estrella blanca en el cuadro azul.",
                pistaMascota = "🇨🇱 ¡Nuestra bandera tiene una hermosa estrella blanca arriba!",
                iconoVisual = "🇨🇱 ⭐",
                pregunta = "¿Cómo es la bandera de Chile?",
                opcionA = "Tiene un sol amarillo al centro",
                opcionB = "Tiene colores blanco, azul, rojo y una estrella",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 5,
                titulo = "Pueblos originarios",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.HISTORIA],
                descripcion = "Valora la historia y herencia de los pueblos originarios.",
                contenido = "En Chile han vivido pueblos originarios como los Mapuche, Aymara, Diaguitas y Rapa Nui, que nos legaron palabras y tradiciones.",
                pistaMascota = "¡El kultrún y el mapudungún pertenecen a un pueblo muy conocido del sur! 🥁",
                iconoVisual = "🥁 🌿",
                pregunta = "¿Cuál de estos es un pueblo originario de Chile?",
                opcionA = "Pueblo Mapuche",
                opcionB = "Pueblo Vikingo",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 6,
                titulo = "Paisajes de Chile",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.HISTORIA],
                descripcion = "Reconoce la diversidad de paisajes del norte al sur.",
                contenido = "Chile es un país largo y angosto: en el norte hay desierto seco, en el centro valles y en el sur bosques y lagos.",
                pistaMascota = "¡En el norte casi no llueve y hay mucha arena! 🏜️",
                iconoVisual = "🏜️ 🏔️",
                pregunta = "¿Qué paisaje caracteriza al norte de Chile?",
                opcionA = "Selva tropical",
                opcionB = "El desierto de Atacama",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 7,
                titulo = "Fiestas y tradiciones",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.HISTORIA],
                descripcion = "Descubre bailes y juegos típicos de nuestras fiestas.",
                contenido = "En septiembre celebramos las Fiestas Patrias con música, cueca, empanadas y juegos típicos como el volantín y el trompo.",
                pistaMascota = "¡Pañuelo en mano! Es el baile nacional de Chile 💃🇨🇱",
                iconoVisual = "🪁 🇨🇱",
                pregunta = "¿Cuál es el baile nacional de Chile?",
                opcionA = "La cueca",
                opcionB = "El tango",
                respuestaCorrecta = "A"
            )
        ),
        Materia.CIENCIAS to mutableListOf(
            Leccion(
                numero = 1,
                titulo = "Seres vivos",
                estado = EstadoLeccion.EN_CURSO,
                objetivoRelacionado = oasEjemplo[Materia.CIENCIAS],
                descripcion = "Diferencia seres vivos de elementos sin vida.",
                contenido = "Los seres vivos nacen, crecen, necesitan agua y alimento, y responden a su entorno. Las piedras no son seres vivos.",
                pistaMascota = "¡Piensa en qué cosa respira, come y crece! 🌱🐶",
                iconoVisual = "🌱 🐶",
                pregunta = "¿Cuál de estos es un ser vivo?",
                opcionA = "Una planta",
                opcionB = "Una piedra",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 2,
                titulo = "Las plantas",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.CIENCIAS],
                descripcion = "Conoce las partes de la planta y lo que necesita para vivir.",
                contenido = "Las plantas tienen raíz, tallo y hojas. Necesitan luz del sol, agua, aire y tierra para crecer sanas.",
                pistaMascota = "¡Está bajo la tierra y absorbe el agüita como una bombilla! 💧",
                iconoVisual = "🌻 💧",
                pregunta = "¿Qué parte de la planta absorbe el agua desde la tierra?",
                opcionA = "La flor",
                opcionB = "La raíz",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 3,
                titulo = "Los animales",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.CIENCIAS],
                descripcion = "Identifica el hábitat donde viven distintos animales.",
                contenido = "Cada animal vive en un lugar adecuado para su cuerpo: los peces viven en el agua y las aves vuelan por el aire.",
                pistaMascota = "¡Mira la pecera! El pez respira bajo el agua 🐟",
                iconoVisual = "🐟 🌊",
                pregunta = "¿Qué animal vive en el agua?",
                opcionA = "El pez",
                opcionB = "El perro",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 4,
                titulo = "El agua",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.CIENCIAS],
                descripcion = "Reconoce los estados del agua y cómo cuidarla.",
                contenido = "El agua puede ser líquida (como en el río), sólida (hielo) o gaseosa (vapor). Cuidar el agua protege la vida.",
                pistaMascota = "¡Cuando metes agua al congelador se pone durita y fría! 🧊",
                iconoVisual = "💧 🧊",
                pregunta = "¿En qué estado está el agua cuando forma hielo?",
                opcionA = "Estado gaseoso",
                opcionB = "Estado sólido",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 5,
                titulo = "El cuerpo humano",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.CIENCIAS],
                descripcion = "Identifica órganos principales y hábitos saludables.",
                contenido = "El corazón bombea sangre a todo el cuerpo y los pulmones nos permiten respirar aire limpio cada segundo.",
                pistaMascota = "¡Respira hondo! Siente cómo se infla tu pecho con aire 🌬️",
                iconoVisual = "🫁 ❤️",
                pregunta = "¿Qué órganos nos permiten respirar aire?",
                opcionA = "Los pulmones",
                opcionB = "Los dientes",
                respuestaCorrecta = "A"
            ),
            Leccion(
                numero = 6,
                titulo = "Los cinco sentidos",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.CIENCIAS],
                descripcion = "Relaciona vista, oído, olfato, gusto y tacto con sus órganos.",
                contenido = "Con los ojos vemos, con los oídos escuchamos, con la nariz olemos, con la lengua saboreamos y con la piel sentimos.",
                pistaMascota = "¡Piensa en qué usas para escuchar el audio de esta lección! 🎧",
                iconoVisual = "👂 🎶",
                pregunta = "¿Qué sentido usamos para escuchar música y sonidos?",
                opcionA = "El gusto",
                opcionB = "El oído",
                respuestaCorrecta = "B"
            ),
            Leccion(
                numero = 7,
                titulo = "El sistema solar",
                estado = EstadoLeccion.BLOQUEADA,
                objetivoRelacionado = oasEjemplo[Materia.CIENCIAS],
                descripcion = "Reconoce el Sol, la Tierra y la Luna en el cielo.",
                contenido = "La Tierra es nuestro planeta y gira alrededor del Sol, que es una gran estrella que nos entrega luz y calor.",
                pistaMascota = "¡Brilla en el día y nos da calorcito! ☀️",
                iconoVisual = "☀️ 🌍",
                pregunta = "¿Qué estrella nos entrega luz y calor durante el día?",
                opcionA = "El Sol",
                opcionB = "La Luna",
                respuestaCorrecta = "A"
            )
        )
    )

    private val listaSolicitudesUtp: MutableList<SolicitudUtp> = mutableListOf(
        SolicitudUtp(
            id = "LENGUAJE_1",
            materia = Materia.LENGUAJE,
            numeroLeccion = 1,
            titulo = "1. Las vocales",
            docente = "Prof. Juan Pérez",
            descripcion = "Módulo de aprendizaje interactivo con apoyo de audio.",
            estado = EstadoRevisionUtp.PENDIENTE
        ),
        SolicitudUtp(
            id = "MATEMATICAS_1",
            materia = Materia.MATEMATICAS,
            numeroLeccion = 1,
            titulo = "Conteo del 1 al 20",
            docente = "Prof. Carlos Ruiz",
            descripcion = "Actividad interactiva con material concreto.",
            estado = EstadoRevisionUtp.APROBADA
        ),
        SolicitudUtp(
            id = "HISTORIA_1",
            materia = Materia.HISTORIA,
            numeroLeccion = 1,
            titulo = "1. Mi familia",
            docente = "Prof. Juan Pérez",
            descripcion = "Reconocimiento del entorno familiar y símbolos patrios.",
            estado = EstadoRevisionUtp.APROBADA
        ),
        SolicitudUtp(
            id = "CIENCIAS_1",
            materia = Materia.CIENCIAS,
            numeroLeccion = 1,
            titulo = "Los sentidos",
            docente = "Prof. Ana Torres",
            descripcion = "Ficha técnica y reconocimiento de texturas.",
            estado = EstadoRevisionUtp.APROBADA
        )
    )

    fun lecciones(materia: Materia): List<Leccion> = mapaLecciones[materia] ?: emptyList()

    fun objetivoPorMateria(materia: Materia): ObjetivoAprendizaje? = oasEjemplo[materia]

    fun marcarComoCompletada(materia: Materia, numeroLeccion: Int) {
        val lista = mapaLecciones[materia] ?: return
        val index = lista.indexOfFirst { it.numero == numeroLeccion }
        if (index != -1) {
            lista[index] = lista[index].copy(estado = EstadoLeccion.COMPLETADA)
            if (index + 1 < lista.size && lista[index + 1].estado == EstadoLeccion.BLOQUEADA) {
                lista[index + 1] = lista[index + 1].copy(estado = EstadoLeccion.EN_CURSO)
            }
        }
    }

    fun guardarLeccionDocente(materia: Materia, leccionEditada: Leccion) {
        val lista = mapaLecciones[materia] ?: return
        val index = lista.indexOfFirst { it.numero == leccionEditada.numero }
        if (index != -1) {
            lista[index] = leccionEditada
        } else {
            lista.add(leccionEditada)
        }

        val idSolicitud = "${materia.name}_${leccionEditada.numero}"
        val solicitudActualizada = SolicitudUtp(
            id = idSolicitud,
            materia = materia,
            numeroLeccion = leccionEditada.numero,
            titulo = "${leccionEditada.numero}. ${leccionEditada.titulo}",
            docente = "Prof. Juan Pérez",
            descripcion = leccionEditada.descripcion,
            estado = EstadoRevisionUtp.PENDIENTE
        )
        val idxUtp = listaSolicitudesUtp.indexOfFirst { it.id == idSolicitud }
        if (idxUtp != -1) {
            listaSolicitudesUtp[idxUtp] = solicitudActualizada
        } else {
            listaSolicitudesUtp.add(0, solicitudActualizada)
        }
    }

    fun obtenerSolicitudesUtp(): List<SolicitudUtp> = listaSolicitudesUtp.toList()

    fun actualizarEstadoSolicitudUtp(id: String, nuevoEstado: EstadoRevisionUtp) {
        val idx = listaSolicitudesUtp.indexOfFirst { it.id == id }
        if (idx != -1) {
            listaSolicitudesUtp[idx] = listaSolicitudesUtp[idx].copy(estado = nuevoEstado)
        }
    }

    fun calcularProgreso(lecciones: List<Leccion>): Int {
        val completadas = lecciones.count { it.estado == EstadoLeccion.COMPLETADA }
        return if (lecciones.isNotEmpty()) (completadas * 100) / lecciones.size else 0
    }
}