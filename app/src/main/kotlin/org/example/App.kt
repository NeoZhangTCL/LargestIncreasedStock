package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okio.Buffer
import okio.Okio
import okio.source
import okio.buffer
import okio.BufferedSource
import java.io.File

class App {
    val greeting: String
        get() {
            return "Hello World! This is Neo"
        }
}

fun main() {
    val filePath = getResourceFilePath("values.csv")
    val numChunks = 4 // Number of chunks for parallel processing

    runBlocking {
        val file = File(filePath)
        val fileLength = file.length()
        val numLines = file.bufferedReader().use { it.readLines().filter { line -> line.isNotBlank() }.size }

        val linesPerChunk = numLines / numChunks

        val jobs = ArrayList<Buffer>()

        // Create a coroutine job for each chunk of the file
        for (i in 0 until numChunks) {
            val startLine = i * linesPerChunk
            val endLine = if (i == numChunks - 1) numLines else ((i + 1) * linesPerChunk)

            val job = async(Dispatchers.IO) {
                val source = file.source().buffer()
                skipLines(source, startLine)
                val buffer = Buffer()
                repeat(endLine - startLine) {
                    var line: String?
                    do {
                        line = source.readUtf8Line()
                    } while (line != null && line.isBlank())
                    if (line != null) {
                        buffer.writeUtf8(line)
                        buffer.writeUtf8("\n")
                    }
                }
                buffer
            }
            jobs.add(job.await())
        }

        // Process the chunks here...
        for (buffer in jobs) {
            val chunkData = buffer.readUtf8()
            // Process chunkData here...
            println("Chunk data: $chunkData")
        }

        // No need to close files opened with Okio
    }
}

fun getResourceFilePath(fileName: String): String {
    val classLoader = object {}.javaClass.classLoader
    val resourceUrl = classLoader.getResource(fileName)
        ?: throw IllegalArgumentException("File not found: $fileName")
    return File(resourceUrl.file).path
}

fun skipLines(source: BufferedSource, startLine: Int) {
    repeat(startLine) {
        source.readUtf8Line() // Skip lines until startLine
    }
}