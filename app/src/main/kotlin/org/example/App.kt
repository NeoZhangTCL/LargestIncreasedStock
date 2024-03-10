package org.example

import kotlinx.coroutines.*
import java.io.File
import java.io.FileReader
import java.io.BufferedReader
import java.util.*
import kotlin.math.max


suspend fun readCsvPart(file: File, startLine: Int, endLine: Int): Map<String, RangeRecord> = withContext(Dispatchers.IO) {
    val map = mutableMapOf<String, RangeRecord>()

    val reader = FileReader(file)
    val br = BufferedReader(reader)
    var lineCount = 0

    try {
        while (true) { 
            val line = br.readLine() ?: break
            if (lineCount in startLine until endLine) {
                if (line.isNotBlank()) {
                    val record = parseStockRecord(line)
                    record?.let {
                        if (map.containsKey(record.name)) {
                            map[record.name]!! += record
                        } else {
                            map[record.name] = RangeRecord(record.name, record, record)
                        }
                    }   
                }
            }
            lineCount++
        }
    } finally {
        br.close()
    }
    
    map
}

fun splitLines(file: File, threads: Int): List<IntArray> {
    val lines = file.readLines().size
    val chunkSize = (lines + threads - 1) / threads
    val chunks = mutableListOf<IntArray>()
    var startLine = 0

    for (i in 0 until threads) {
        val endLine = minOf(startLine + chunkSize, lines)
        chunks.add(intArrayOf(startLine, endLine - 1))
        startLine = endLine
    }

    return chunks
}

fun main() = runBlocking {
    val fileName = "values.csv"
    val threads = 4
    val file = File(getResourceFilePath(fileName))
    val chunks = splitLines(file, threads)

    val jobs = chunks.map { chunk ->
        async {
            readCsvPart(file, chunk[0], chunk[1] + 1)
        }
    }

    val results = jobs.map { it.await() }

    findLargestIncreased(results)
}

fun findLargestIncreased(maps: List<Map<String, RangeRecord>>) {
    val mergedMap = LinkedHashMap<String, RangeRecord>()
    val increaseMap = LinkedHashMap<String, Double>()

    var company = ""
    var largestIncreased = 0.0

    maps.forEach { map: Map<String, RangeRecord> ->
        map.forEach { (key, value) ->
            if (mergedMap.containsKey(key)) {
                if (mergedMap[key] is RangeRecord) {
                    mergedMap[key] = value + mergedMap[key] as RangeRecord
                    increaseMap[key] = max(mergedMap[key]!!.lastRecord.value - mergedMap[key]!!.firstRecord.value, 0.0)
                    if (increaseMap[key]!! > largestIncreased) {
                        company = key
                        largestIncreased = increaseMap[key]!!
                    }
                } else {
                    throw IllegalArgumentException("Cannot merge non-numeric values for key: $key")
                }
            } else {
                mergedMap[key] = value
                increaseMap[key] = max(value.lastRecord.value - value.firstRecord.value, 0.0)
                if (increaseMap[key]!! > largestIncreased) {
                    company = key
                    largestIncreased = increaseMap[key]!!
                }
            }
        }
    }

    if (company != "") {
        println("Company:$company Increased:$largestIncreased")
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