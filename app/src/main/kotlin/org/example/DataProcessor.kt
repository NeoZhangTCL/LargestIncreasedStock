package org.example

class DataProcessor {

    internal val earliestRecords = mutableMapOf<String, StockRecord>()
    internal val latestRecords = mutableMapOf<String, StockRecord>()

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