# Kata 3: Transformación de Datos a JSON/GSON

## 1. Objetivo de la Entrega
El objetivo principal de esta entrega es desarrollar una solución software sólida en **Java 25** aplicando los principios de la **Arquitectura Hexagonal (Ports & Adapters)**. La aplicación realiza el consumo de datos de usuarios a través de un servicio externo web, transforma los datos nativos (`UserDto`) en entidades del dominio inmutables (`User`), valida los registros descartando información corrupta o incompleta, y serializa los datos procesados a un formato **JSON estructurado con metadatos** (conteo total de usuarios y fecha de generación).

---

## 2. Dependencias y Versión de JDK
* **Java Development Kit (JDK):** Versión 25.
* **Gestor de Construcción:** Maven (v3.x o superior).
* **Dependencias Externas:**
    * `com.google.code.gson:gson:2.15.0` (Librería elegida para el parseo, manipulación y serialización de JSON con *pretty printing*).

---

## 3. Estructura de la Entrega y Clases Principales

El proyecto sigue una estricta separación de responsabilidades para garantizar un bajo acoplamiento:

```text
src/main/java/software/ulpgc/kata3/
├── domain/model/
│   └── User.java              # Record del dominio (nombre, fecha de nacimiento YYYY-MM-DD, edad y género)
├── ports/
│   ├── UserStore.java         # Puerto de entrada para la carga abstracta de usuarios
│   └── UserDisplay.java       # Puerto de salida para la presentación de datos
├── adapters/
│   ├── RandomUserStore.java   # Adaptador que consume la API HTTPS y aplica reglas de validación
│   └── ConsoleUserDisplay.java# Adaptador de salida por consola
├── UserDto.java               # DTO para mapear la respuesta JSON cruda de la API RandomUser
├── UserResponseDto.java       # DTO contenedor para incorporar el bloque de metadatos al JSON final
└── Main.java                  # Punto de entrada que ensambla los componentes y ejecuta la aplicación
```

---

## 4. Compilación y Ejecución

### Requisitos
Tener instalado JDK 25 y Maven utilizando el entorno IntelliJ IDEA.

Se ejecuta directamente la clase `Main.java` desde el IDE (Mayús + F10).

---

## 5. Repetición de la Kata y Variaciones Introducidas

En el marco de la metodología de **repetición deliberada**:
* **Proceso de repetición:** Se reconstruyó el flujo de desarrollo de forma incremental en Git a través de la rama `develop` mediante 6 commits bien definidos antes de realizar el *merge* a la rama `master` en cada repositorio clonado tras el original.
* **Variaciones pequeñas introducidas:**
    1. Se ampliaron los campos del modelo de dominio `User` incorporando **edad (`age`)** y **género (`gender`)** además del nombre y la fecha de nacimiento.
    2. Se aplicó un formateo directo de la fecha de nacimiento para presentar únicamente el formato clásico **`YYYY-MM-DD`**.
    3. Se añadió un control de errores mediante `JsonParser` para prevenir fallos por respuestas mal formadas (`MalformedJsonException` o redirecciones HTTP).

---

## 6. Evidencias de Verificación y Pruebas Realizadas

Se han realizado pruebas de ejecución para asegurar la robustez del sistema frente a distintos escenarios:

### Ejemplo de Salida JSON Generada
```json
{
  "metadata": {
    "totalCount": 5,
    "generatedAt": "2026-10-29T14:34:59.722Z"
  },
  "users": [
    {
      "name": "Laila Castro",
      "birthday": "1946-07-02",
      "age": 80,
      "gender": "female"
    },
    {
      "name": "Begüm Kocabıyık",
      "birthday": "1955-11-08",
      "age": 70,
      "gender": "female"
    },
    {
      "name": "Luke Brown",
      "birthday": "1958-08-05",
      "age": 68,
      "gender": "male"
    },
    {
      "name": "Aya Husa",
      "birthday": "1946-10-27",
      "age": 80,
      "gender": "female"
    },
    {
      "name": "Anika Taj",
      "birthday": "1985-08-24",
      "age": 41,
      "gender": "female"
    }
  ]
}
```

### Pruebas de Borde Realizadas
* **Validación de nulos:** Verificación de descartes automáticos en el método `toUser()` si la API retorna campos fundamentales vacíos.
* **Manejo de protocolo seguro:** Corrección del endpoint a `https://randomuser.me/api` para evitar fallos de parseo al recibir respuestas HTML por redirección.

---

## 7. Enlace al Vídeo Explicativo
El vídeo demostrativo con la explicación del diseño, separación de responsabilidades, validaciones e historial de commits (duración < 7 minutos) está disponible en:

**(https://youtu.be/crzC3FbJdnc?si=tSbAFtnqzXTUUzjm)**