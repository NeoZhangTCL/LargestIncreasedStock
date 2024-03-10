package org.example

import kotlin.system.measureTimeMillis

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
