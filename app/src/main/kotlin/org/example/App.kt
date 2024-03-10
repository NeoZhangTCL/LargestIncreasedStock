package org.example

import java.io.File

fun main() {
    val fileName = "values.csv"
    val earliestRecords = mutableMapOf<String, StockRecord>()
    val latestRecords = mutableMapOf<String, StockRecord>()

    // Read the file and process each line
    File(getResourceFilePath(fileName)).readLines().forEach { line ->
        parseStockRecord(line)?.let {
            val (earliestRecord, latestRecord) = earliestRecords[it.name] to latestRecords[it.name]
            if (earliestRecord == null || it.date < earliestRecord.date) {
                earliestRecords[it.name] = it
            }
            if (latestRecord == null || it.date > latestRecord.date) {
                latestRecords[it.name] = it
            }
        }
    }

    // Find the stock with the largest increase
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

    // Print the result
    if (maxIncreaseCompany.isNotBlank()) {
        println("Company: $maxIncreaseCompany Increased: $maxIncreaseValue")
    } else {
        println("nil")
    }
}

fun getResourceFilePath(fileName: String): String {
    val classLoader = object {}.javaClass.classLoader
    val resourceUrl = classLoader.getResource(fileName)
        ?: throw IllegalArgumentException("File not found: $fileName")
    return File(resourceUrl.file).path
}