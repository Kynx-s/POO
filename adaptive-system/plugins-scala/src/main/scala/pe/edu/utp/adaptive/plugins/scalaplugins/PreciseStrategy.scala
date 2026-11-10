package pe.edu.utp.adaptive.plugins.scalaplugins

import pe.edu.utp.adaptive.api.{Context, Result, Strategy}

class PreciseStrategy extends Strategy[String, String]:

  private val MaxBytes = 5_000_000L

  override def id(): String = "precise"

  override def supports(context: Context): Boolean =
    context.sizeBytes() <= MaxBytes

  override def execute(input: String): Result[String] =
    val start = System.nanoTime()
    val words = input.split("\\s+").filter(_.nonEmpty)
    val lines = input.linesIterator.size
    val unique = words.map(_.toLowerCase).toSet.size
    val avgLength =
      if words.isEmpty then 0.0 else words.map(_.length).sum.toDouble / words.length
    val summary =
      f"[precise] palabras=${words.length}, unicas=$unique, lineas=$lines, longitud_media=$avgLength%.2f"
    val elapsedMillis = (System.nanoTime() - start) / 1_000_000
    new Result[String](summary, id(), elapsedMillis)
