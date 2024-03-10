package org.example

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.io.File

class FileLoader(
    private val fileName: String,
    private val chunkSize: Int,
    private val dataProcessor: DataProcessor
) {
    suspend fun processFile() {
        val file = File(getResourceFilePath(fileName))
        file.useLines { lines ->
            coroutineScope {
                val jobs = mutableListOf<Job>()
                lines.chunked(chunkSize).forEach { chunk ->
                    jobs += launch {
                        chunk.forEach { line ->
                            parseStockRecord(line)?.let { record ->
                                dataProcessor.updateRecords(record)
                            }
                        }
                    }
                }
                jobs.joinAll()
            }
        }
    }

    private fun getResourceFilePath(fileName: String): String {
        val classLoader = javaClass.classLoader
        val resourceUrl = classLoader.getResource(fileName)
            ?: throw IllegalArgumentException("File not found: $fileName")
        return File(resourceUrl.file).path
    }
}