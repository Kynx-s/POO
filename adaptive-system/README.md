# Adaptive Document Processor

Aplicación Java que procesa documentos de texto y **cambia automáticamente de plugin**
(Java o Scala) según lo que mide del documento. Proyecto final de POO.

**Integrantes:** (completar)

## Cómo funciona

```
Documento → Monitor → Reglas → Reflection → Plugin → Resultado → Registro (JDBC)
```

| Métrica medida | Regla | Plugin elegido |
|---|---|---|
| Líneas dañadas > 5 % | Documento dañado | `SafeStrategy` (Java) |
| Modo precisión y tamaño ≤ 5 MB | Análisis detallado | `PreciseStrategy` (**Scala**) |
| Ninguna aplica | Condiciones normales | `FastStrategy` (Java) |

El núcleo solo conoce la interfaz `Strategy`. Los plugins se registran en
`adaptive-core/src/main/resources/plugins.properties` y se cargan con Reflection.

## Módulos

| Módulo | Contenido |
|---|---|
| `plugin-api` | Contrato Java: `Strategy`, `Context`, `Result` |
| `plugins-java` | `FastStrategy`, `SafeStrategy` |
| `plugins-scala` | `PreciseStrategy` |
| `adaptive-core` | `Monitor`, reglas, `PluginRegistry` (Reflection), `AdaptiveEngine` |
| `persistence` | JDBC con H2: CRUD de registros de procesamiento |
| `launcher` | `Main` con los tres escenarios |

## Requisitos

- JDK 21
- No hace falta instalar Maven ni Scala: el wrapper y Maven los descargan.

## Compilar y probar

```
mvnw.cmd clean verify        (Windows)
./mvnw clean verify          (Linux / macOS)
```

## Ejecutar

```
mvnw.cmd clean install
mvnw.cmd -pl launcher exec:java
```

## Base de datos

Usa H2 (archivo local en `data/`), sin instalar nada. Para cambiar la conexión, define
las variables de entorno `DB_URL`, `DB_USER` y `DB_PASSWORD` (ver `.env.example`).
Nunca subas credenciales reales a GitHub.

## Documentación

- Problema y requisitos: `docs/problema-y-requisitos.md`
- Diagrama de clases: `docs/diagrams/clases.md`
- Documentación del código: con Doxygen y Graphviz instalados, ejecuta `doxygen Doxyfile`
  y abre `docs/doxygen/html/index.html`. Doxygen no interpreta bien los `record` de Java
  ni el plugin Scala.
