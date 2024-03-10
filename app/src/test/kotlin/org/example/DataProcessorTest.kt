/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package org.example

import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*
import kotlin.test.Test

class DataProcessorTest {

    @Test
    fun `test updateRecords`() {
        val dataProcessor = DataProcessor()
        val record1 = StockRecord("AAPL", createDate(2024, 2, 28), 100.0)
        val record2 = StockRecord("AAPL", createDate(2024, 3, 1), 110.0)

        dataProcessor.updateRecords(record1)
        dataProcessor.updateRecords(record2)

        assertEquals(record1, dataProcessor.earliestRecords["AAPL"])
        assertEquals(record2, dataProcessor.latestRecords["AAPL"])
    }

    @Test
    fun `test findLargestIncrease`() {
        val dataProcessor = DataProcessor()
        val record1 = StockRecord("AAPL", createDate(2024, 2, 28), 100.0)
        val record2 = StockRecord("AAPL", createDate(2024, 3, 1), 110.0)
        val record3 = StockRecord("GOOG", createDate(2024, 2, 28), 500.0)
        val record4 = StockRecord("GOOG", createDate(2024, 3, 1), 520.0)

        dataProcessor.updateRecords(record1)
        dataProcessor.updateRecords(record2)
        dataProcessor.updateRecords(record3)
        dataProcessor.updateRecords(record4)

        val result = dataProcessor.findLargestIncrease()

        assertEquals("GOOG" to 20.0, result)
    }

    @Test
    fun `test findLargestIncrease - no records`() {
        val dataProcessor = DataProcessor()

        val result = dataProcessor.findLargestIncrease()

        assertEquals("" to 0.0, result)
    }

    private fun createDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year + 1900, month, day)
        return calendar.time
    }
}
