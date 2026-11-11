# Problema y requisitos

## Problema

Un procesador de documentos de texto debe analizar archivos muy distintos: unos sanos y
pequeños, otros dañados, otros que requieren un análisis más detallado. Un único algoritmo
no sirve bien para todos: el rápido ignora errores, el cuidadoso es más lento, y el preciso
cuesta más cómputo.

## Solución

Una aplicación Java que mide el documento, decide qué implementación conviene y la carga
en tiempo de ejecución con Java Reflection. Las implementaciones cumplen el mismo contrato
(`Strategy`); dos están escritas en Java y una en Scala, en la misma JVM.

## Métricas, reglas y escenarios

| Métrica | Descripción |
|---|---|
| Tamaño | Bytes del documento |
| Tasa de error | Proporción de líneas con caracteres de control |

| Regla | Condición | Plugin |
|---|---|---|
| 1 | Error > 5 % | `safe` (Java) |
| 2 | Modo precisión y tamaño ≤ 5 MB | `precise` (Scala) |
| Por defecto | Ninguna aplica | `fast` (Java) |

| Escenario | Entrada | Plugin esperado |
|---|---|---|
| 1 | Documento sano | `fast` |
| 2 | 2 de 3 líneas dañadas | `safe` |
| 3 | Documento sano en modo precisión | `precise` |

## Requisitos funcionales

1. RF1: medir tamaño y tasa de error de un documento.
2. RF2: elegir un plugin según reglas de adaptación.
3. RF3: descubrir, validar y crear el plugin con Reflection.
4. RF4: ejecutar el plugin y devolver resultado, plugin usado y tiempo.
5. RF5: guardar cada procesamiento en una base de datos (crear, leer, actualizar, borrar).
6. RF6: mostrar un registro de qué plugin se usó y por qué.

## Requisitos no funcionales

1. RNF1: Java 21, Scala 3.3 y Maven con versiones fijas.
2. RNF2: el núcleo depende solo del contrato, nunca de una clase concreta.
3. RNF3: compilación y pruebas con `mvnw clean verify`.
4. RNF4: sin credenciales en el repositorio.
5. RNF5: pruebas automáticas con JUnit 5.

## Requisitos de la consigna

- [x] Contrato de plugin definido en Java
- [x] Dos plugins Java y uno Scala intercambiables
- [x] Reflection para descubrir, validar y construir
- [x] Dos métricas, dos reglas y tres escenarios
- [x] CRUD JDBC sin credenciales en GitHub
- [x] JUnit 5 y `clean verify` exitoso
- [x] Doxygen y README
- [x] Encapsulamiento, interfaces, clase abstracta y polimorfismo
- [x] Colecciones, genéricos, streams y lambdas
- [ ] UML coherente con el código (ver `docs/diagrams/clases.md`; revisar al final)
- [ ] `docs/architecture.md`, `db/schema.sql` y reporte de cobertura (entrega final)
