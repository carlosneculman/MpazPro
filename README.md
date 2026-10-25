# MPAZ PRO 3.º - Plataforma Móvil de Apoyo Didáctico y Seguimiento del Aprendizaje

MPAZ PRO 3.º es un producto mínimo viable (MVP) en formato de aplicación móvil diseñado para apoyar la labor pedagógica en la Escuela Escritora Marcela Paz (San Bernardo). La solución busca complementar los procesos de aprendizaje en el nivel de Tercero Básico organizando recursos educativos, actividades prácticas y evaluaciones vinculadas a los Objetivos de Aprendizaje (OA) del Currículum Nacional.

---

## 1. Contexto y Diagnóstico

Actualmente, las actividades de consolidación de contenidos se realizan de forma predominantemente impresa. Aunque este método permite trabajar las materias, la revisión manual fragmenta los datos y dificulta el registro continuo del avance por cada Objetivo de Aprendizaje. 

Esta situación genera los siguientes inconvenientes:
* Falta de trazabilidad inmediata sobre el nivel de logro en cada objetivo curricular.
* Complejidad para identificar con rapidez qué contenidos requieren reforzamiento.
* Dificultad para adaptar la entrega de instrucciones y materiales a los distintos ritmos de aprendizaje del alumnado.

---

## 2. Propuesta de Solución

La aplicación móvil centraliza la entrega de contenidos y el registro de progreso mediante una interfaz adaptada para educación primaria. Ofrece retroalimentación inmediata sobre respuestas correctas e incorrectas, permite fijar límites de intentos en las evaluaciones y consolida la información de rendimiento en un reporte orientado al equipo docente.

---

## 3. Alcance Curricular (Versión MVP)

El alcance del proyecto se limita a la representación de **una unidad temática por asignatura** para Tercero Básico:

1. **Lenguaje y Comunicación:** 1 unidad curricular vinculada a OA oficiales.
2. **Matemática:** 1 unidad curricular vinculada a OA oficiales.
3. **Ciencias Naturales:** 1 unidad curricular vinculada a OA oficiales.
4. **Historia, Geografía y Ciencias Sociales:** 1 unidad curricular vinculada a OA oficiales.

---

## 4. Perfiles y Permisos de Usuario

El sistema contempla tres perfiles de uso claramente delimitados:

### Jefatura de UTP (Unidad Técnico-Pedagógica)
* Revisa y autoriza los contenidos pedagógicos.
* Verifica la trazabilidad y pertenencia de las actividades con los Objetivos de Aprendizaje.

### Docente de Asignatura
* Selecciona los Objetivos de Aprendizaje disponibles.
* Asigna contenidos, ejercicios sencillos y desafíos progresivos.
* Configura la cantidad máxima de intentos permitidos para la prueba final.
* Consulta, descarga y valida los informes de rendimiento pedagógico.

### Estudiante
* Accede al material didáctico asignado para su curso.
* Resuelve actividades de ejercitación y desafíos de complejidad gradual.
* Rinde la evaluación final de la unidad dentro del número de intentos autorizados.
* Visualiza la corrección de sus respuestas (correctas/incorrectas).
* *Nota:* El perfil de estudiante no puede modificar contenidos ni acceder a los informes docentes.

---

## 5. Criterios de Diseño y Experiencia de Usuario (UX/UI)

Para responder a las necesidades formativas de estudiantes de 8 a 9 años, la interfaz cumple con los siguientes parámetros:

* **Accesibilidad e Instrucciones:** Tipografía legible, textos breves e instrucciones directas.
* **Apoyo Multimodal:** Incorporación de audios explicativos para acompañar la lectura (sin sustituir la lectura autónoma) y apoyos visuales/gráficos claros.
* **Navegación Intuitiva:** Botones destacados y flujos de pantalla simplificados para uso en teléfonos y tabletas Android.
* **Confirmación de Progreso:** Mensajes claros de confirmación al completar módulos y ejercicios.

---

## 6. Consideraciones de Seguridad, Datos y Limitaciones Pedagógicas

* **Información Ficticia:** La plataforma funciona de manera independiente y utiliza identificadores, listas de cursos y resultados ficticios. No está conectada a bases de datos reales del establecimiento ni al Libro de Clases.
* **Privacidad de Hardware:** La aplicación no requiere permisos de cámara, GPS, micrófono ni recolección de datos biométricos.
* **Uso del Informe Pedagógico:** El informe generado es un instrumento de apoyo exclusivo para el cuerpo docente y requiere validación profesional. La aplicación **no realiza diagnósticos automáticos** ni toma decisiones o sanciones académicas de forma autónoma.
* **Derechos de Autor:** Los recursos gráficos, audios y textos son de elaboración propia, de dominio público o cuentan con la autorización correspondiente.

---

## 7. Requisitos del Sistema e Instalación

### Requisitos Previos
* Dispositivos Android (smartphones o tabletas) con conexión a internet.
* Entorno de desarrollo para compilación: Node.js / Android Studio (según el framework utilizado en el repositorio).
