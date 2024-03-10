package org.example

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

data class StockRecord(
    val name: String,
    val date: Date,
    val value: Double
)

fun parseStockRecord(rawRecord: String): StockRecord? {
    val fields = rawRecord.split(',')

    if (fields.size != 5) {
        return null
    }

    val name = fields[0]
    val dateString = fields[1]
    val valueStr = fields[3]

    if (name.isBlank()) return null

    // Parse the date
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = try {
        dateFormat.parse(dateString)
    } catch (e: ParseException) {
        return null // Return null if the date is invalid
    }

    // Parse the value
    val value = try {
        valueStr.toDouble()
    } catch (e: NumberFormatException) {
        return null // Return null if the value is not a double
    }

    // If both date and value are valid, return the StockRecord object
    return StockRecord(name, date, value)
}