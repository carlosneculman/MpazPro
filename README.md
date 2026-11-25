# MPAZ PRO 3.º
### Plataforma Móvil de Apoyo Didáctico y Seguimiento del Aprendizaje

**MPAZ PRO 3.º** es una aplicación móvil desarrollada en Kotlin y Jetpack Compose bajo arquitectura MVVM, diseñada para apoyar la labor pedagógica en la Escuela Escritora Marcela Paz (San Bernardo). La solución complementa los procesos de aprendizaje en Tercero Básico organizando recursos educativos, actividades prácticas, gamificación y evaluaciones vinculadas a los Objetivos de Aprendizaje (OA) del Currículum Nacional.

## 1. Contexto y Diagnóstico
Actualmente, las actividades de consolidación de contenidos se realizan de forma predominantemente impresa. Aunque este método permite trabajar las materias, la revisión manual fragmenta los datos y dificulta el registro continuo del avance por cada Objetivo de Aprendizaje.

Esta situación genera los siguientes inconvenientes:
- Falta de trazabilidad inmediata sobre el nivel de logro en cada objetivo curricular.
- Complejidad para identificar con rapidez qué contenidos requieren reforzamiento.
- Dificultad para adaptar la entrega de instrucciones y materiales a los distintos ritmos de aprendizaje del alumno.

## 2. Propuesta de Solución
La aplicación móvil centraliza la entrega de contenidos, la gamificación del aprendizaje y el registro de progreso mediante una interfaz adaptada para educación primaria. Todas las asignaturas comienzan en 0% con desbloqueo progresivo de lecciones a medida que el alumno avanza. Ofrece retroalimentación inmediata sobre respuestas correctas e incorrectas, recompensas coleccionables al completar lecciones, carga de audios explicativos por parte de los docentes, revisión de planificaciones por UTP e informes de rendimiento consolidados.

## 3. Alcance Curricular
El proyecto contempla una unidad temática por asignatura para Tercero Básico:
1. **Lenguaje y Comunicación:** 1 unidad curricular vinculada a OA oficiales.
2. **Matemática:** 1 unidad curricular vinculada a OA oficiales.
3. **Ciencias Naturales:** 1 unidad curricular vinculada a OA oficiales.
4. **Historia, Geografía y Ciencias Sociales:** 1 unidad curricular vinculada a OA oficiales.

## 4. Perfiles y Permisos de Usuario
El sistema contempla tres perfiles de uso delimitados:

### Jefatura de UTP (Unidad Técnico-Pedagógica)
- Revisa las solicitudes pendientes y autoriza los contenidos pedagógicos enviados por los profesores.
- Filtra las planificaciones por asignatura (Lenguaje, Matemáticas, Historia y Ciencias).
- Escucha los audios adjuntos por el docente directamente desde la solicitud antes de aprobar el OA o solicitar cambios.
- Verifica la trazabilidad y pertenencia de las actividades con los Objetivos de Aprendizaje.

### Docente de Asignatura
- Alterna entre las 4 asignaturas para gestionar y editar las lecciones del curso.
- Selecciona los Objetivos de Aprendizaje disponibles y personaliza títulos, descripciones e instrucciones.
- Sube archivos de audio propios desde el teléfono para cada lección sin límite de duración, con vista previa del reproductor.
- Consulta, descarga en PDF y valida los informes de rendimiento pedagógico.

### Estudiante
- Accede al material didáctico asignado para su curso, iniciando con el progreso en 0% y desbloqueando etapas a medida que completa cada lección.
- Escucha el audio de apoyo de la clase con controles para pausar, adelantar o retroceder.
- Resuelve actividades de ejercitación y visualiza al instante la corrección de sus respuestas.
- Desbloquea medallas Pokémon al completar lecciones y consulta su colección en la vitrina de su perfil.
- Personaliza su foto de perfil desde la cámara o galería, configura ajustes visuales de accesibilidad y activa notificaciones de recordatorio de estudio.
- *Nota:* El perfil de estudiante no puede modificar contenidos ni acceder a los informes docentes.

## 5. Arquitectura y Stack Tecnológico
## 5. Arquitectura y Stack Tecnológico
- **Cliente Móvil:** Kotlin, Jetpack Compose, Material Design 3, arquitectura MVVM (`StateFlow`, `ViewModel`, `Lifecycle`), Retrofit 2, Gson y Coil.
- **Backend y Persistencia:** API REST en Node.js con Express (estructura modular `routes/` y `controllers/`), conectada a PostgreSQL y desplegada en AWS EC2.
  - **Repositorio Backend:** `https://github.com/crisHermA02/backend-eltarugo`
- **Integración Externa:** Consumo de **PokéAPI** para la obtención de sprites oficiales y metadatos de recompensas.

### Endpoints Principales de la API
| Método | Ruta | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Autenticación de usuarios y retorno de datos de sesión |
| `POST` | `/api/auth/registro` | Registro de nuevas cuentas de usuario |
| `GET` | `/api/medallas/:id_user` | Consulta el historial de medallas Pokémon de un estudiante |
| `POST` | `/api/medallas/desbloquear` | Registra una nueva medalla Pokémon en PostgreSQL |
### Endpoints Principales de la API
| Método | Ruta | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Autenticación de usuarios y retorno de datos de sesión |
| `POST` | `/api/auth/registro` | Registro de nuevas cuentas de usuario |
| `GET` | `/api/medallas/:id_user` | Consulta el historial de medallas Pokémon de un estudiante |
| `POST` | `/api/medallas/desbloquear` | Registra una nueva medalla Pokémon en PostgreSQL |

## 6. Criterios de Diseño y Experiencia de Usuario (UX/UI)
Para responder a las necesidades formativas de estudiantes de 8 a 9 años, la interfaz cumple con los siguientes parámetros:
- **Accesibilidad e Instrucciones:** Tipografía legible, textos breves, temas visuales configurables e instrucciones directas.
- **Apoyo Multimodal:** Reproductor de audio nativo para acompañar la lectura y apoyos visuales claros.
- **Navegación Intuitiva:** Botones destacados, validaciones en formularios y flujos simplificados para uso en teléfonos y tabletas Android.
- **Refuerzo Positivo:** Confirmación visual inmediata al completar módulos, desbloqueo de medallas coleccionables y notificaciones locales de recordatorio.

## 7. Consideraciones de Seguridad, Datos y Limitaciones Pedagógicas
- **Datos de Demostración:** La plataforma utiliza identificadores, cursos y resultados de prueba. No está conectada a sistemas oficiales ni al Libro de Clases del establecimiento.
- **Privacidad y Permisos:** La selección de foto de perfil y archivos de audio utiliza los selectores nativos de Android. El permiso de notificaciones se solicita únicamente si el usuario activa los recordatorios de estudio.
- **Uso del Informe Pedagógico:** El informe generado es un instrumento de apoyo para el cuerpo docente y requiere validación profesional.
- **Derechos de Autor:** Los recursos gráficos y textos son de elaboración propia, dominio público o provienen de APIs abiertas con fines educativos.

## 8. Requisitos, Instalación y Compilación

### Requisitos previos
- Dispositivo o emulador Android (API 24 o superior).
- Android Studio con soporte para Kotlin y Jetpack Compose.
- Instancia activa del servidor backend ([repositorio backend-eltarugo](https://github.com/crisHermA02/backend-eltarugo)) y PostgreSQL.

### Configuración de red
1. **URL del Backend:** Verificar que `BASE_URL` en `RetrofitClient.kt` apunte a la IP pública activa del servidor.
