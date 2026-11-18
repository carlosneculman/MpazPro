# MPAZ PRO 3.º
### Plataforma Móvil de Apoyo Didáctico y Seguimiento del Aprendizaje

**MPAZ PRO 3.º** es un producto mínimo viable (MVP) en formato de aplicación móvil diseñado para apoyar la labor pedagógica en la Escuela Escritora Marcela Paz (San Bernardo). La solución busca complementar los procesos de aprendizaje en el nivel de Tercero Básico organizando recursos educativos, actividades prácticas y evaluaciones vinculadas a los Objetivos de Aprendizaje (OA) del Currículum Nacional.

## 1. Contexto y Diagnóstico
Actualmente, las actividades de consolidación de contenidos se realizan de forma predominantemente impresa. Aunque este método permite trabajar las materias, la revisión manual fragmenta los datos y dificulta el registro continuo del avance por cada Objetivo de Aprendizaje.

Esta situación genera los siguientes inconvenientes:
- Falta de trazabilidad inmediata sobre el nivel de logro en cada objetivo curricular.
- Complejidad para identificar con rapidez qué contenidos requieren reforzamiento.
- Dificultad para adaptar la entrega de instrucciones y materiales a los distintos ritmos de aprendizaje del alumno.

## 2. Propuesta de Solución
La aplicación móvil centraliza la entrega de contenidos y el registro de progreso mediante una interfaz adaptada para educación primaria. Todas las asignaturas comienzan en 0% con desbloqueo progresivo de lecciones a medida que el alumno avanza. Además, ofrece retroalimentación inmediata sobre respuestas correctas e incorrectas, permite a los docentes subir audios explicativos desde su dispositivo para cada clase, conecta las planificaciones con la revisión de UTP y consolida la información de rendimiento en un informe orientado al equipo docente.

## 3. Alcance Curricular (Versión MVP)
El alcance del proyecto se limita a la representación de una unidad temática por asignatura para Tercero Básico:
1. **Lenguaje y Comunicación:** 1 unidad curricular vinculada a OA oficiales.
2. **Matemática:** 1 unidad curricular vinculada a OA oficiales.
3. **Ciencias Naturales:** 1 unidad curricular vinculada a OA oficiales.
4. **Historia, Geografía y Ciencias Sociales:** 1 unidad curricular vinculada a OA oficiales.

## 4. Perfiles y Permisos de Usuario
El sistema contempla tres perfiles de uso claramente delimitados:

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
- Resuelve actividades de ejercitación y visualiza al instante la corrección de sus respuestas (correctas/incorrectas).
- Personaliza su foto de perfil desde la cámara o galería y activa notificaciones de recordatorio de estudio.
- *Nota:* El perfil de estudiante no puede modificar contenidos ni acceder a los informes docentes.

## 5. Criterios de Diseño y Experiencia de Usuario (UX/UI)
Para responder a las necesidades formativas de estudiantes de 8 a 9 años, la interfaz cumple con los siguientes parámetros:
- **Accesibilidad e Instrucciones:** Tipografía legible, textos breves e instrucciones directas.
- **Apoyo Multimodal:** Incorporación de un reproductor de audio nativo para acompañar la lectura (con el audio subido por el profesor) y apoyos visuales claros.
- **Navegación Intuitiva:** Botones destacados, validaciones en los formularios y flujos de pantalla simplificados bajo estructura MVVM para uso en teléfonos y tabletas Android.
- **Confirmación de Progreso:** Mensajes claros de confirmación al completar módulos y notificaciones locales para recordar el estudio.

## 6. Consideraciones de Seguridad, Datos y Limitaciones Pedagógicas
- **Información ficticia:** La plataforma funciona de manera independiente y utiliza identificadores, listas de cursos y resultados ficticios. No está conectada a bases de datos reales del establecimiento ni al Libro de Clases.
- **Privacidad y Permisos:** La selección de foto de perfil y de archivos de audio utiliza el selector y la cámara propios del sistema Android, guardando los archivos solo de forma interna en la aplicación. Únicamente se solicita permiso de notificaciones si el usuario decide activar los recordatorios de estudio en su perfil.
- **Uso del Informe Pedagógico:** El informe generado es un instrumento de apoyo exclusivo para el cuerpo docente y requiere validación profesional. La aplicación no realiza diagnósticos automáticos ni toma de decisiones o sanciones académicas de forma autónoma.
- **Derechos de Autor:** Los recursos gráficos, audios y textos son de elaboración propia, de dominio público o cuentan con la autorización correspondiente.

## 7. Requisitos del Sistema e Instalación

### Requisitos previos
- Dispositivos o emulador Android (smartphones o tabletas).
- Entorno de desarrollo para compilación: Android Studio (Kotlin y Jetpack Compose).

### Credenciales de prueba
- **Jefatura UTP:** `utp@mpaz.cl`
- **Docente:** `profesor@mpaz.cl` (o `docente@mpaz.cl`)
- **Estudiante:** `estudiante@mpaz.cl`
*(Cualquier contraseña de 4 o más caracteres).*
