package org.example

import kotlinx.coroutines.*
import java.io.File
import kotlin.system.measureTimeMillis

class DataProcessor {
    private val earliestRecords = mutableMapOf<String, StockRecord>()
    private val latestRecords = mutableMapOf<String, StockRecord>()

    fun updateRecords(record: StockRecord) {
        synchronized(earliestRecords) {
            val earliestRecord = earliestRecords[record.name]
            if (earliestRecord == null || record.date < earliestRecord.date) {
                earliestRecords[record.name] = record
            }
        }
        synchronized(latestRecords) {
            val latestRecord = latestRecords[record.name]
            if (latestRecord == null || record.date > latestRecord.date) {
                latestRecords[record.name] = record
            }
        }
    }

    fun findLargestIncrease(): Pair<String, Double> {
        var maxIncreaseCompany = ""
        var maxIncreaseValue = 0.0
        earliestRecords.forEach { (name, earliestRecord) ->
            val latestRecord = latestRecords[name]
            if (latestRecord != null) {
                val increase = latestRecord.value - earliestRecord.value
                if (increase > maxIncreaseValue) {
                    maxIncreaseCompany = name
                    maxIncreaseValue = increase
                }
            }
        }
        return maxIncreaseCompany to maxIncreaseValue
    }
}

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

suspend fun main() {
    val fileName = "values.csv"
    val chunkSize = 1000
    val dataProcessor = DataProcessor()
    val fileLoader = FileLoader(fileName, chunkSize, dataProcessor)

    val time = measureTimeMillis {
        fileLoader.processFile()
    }

    println("File processed in $time milliseconds")

    val (maxIncreaseCompany, maxIncreaseValue) = dataProcessor.findLargestIncrease()

    // Print the result
    if (maxIncreaseCompany.isNotBlank()) {
        println("Company: $maxIncreaseCompany Increased: $maxIncreaseValue")
    } else {
        println("nil")
    }
}
