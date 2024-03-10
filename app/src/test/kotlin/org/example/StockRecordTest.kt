/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package org.example

import org.junit.jupiter.api.Assertions.assertNotNull
import java.text.SimpleDateFormat
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StockRecordTest {

    @Test
    fun `test valid stock record`() {
        val rawRecord = "KCT,2015-7-20,notes,914.96,INCREASED"
        val expectedRecord = StockRecord("KCT", SimpleDateFormat("yyyy-MM-dd").parse("2015-7-20"), 914.96)

        val parsedRecord = parseStockRecord(rawRecord)

        assertEquals(expectedRecord, parsedRecord)
    }

    @Test
    fun `test invalid stock record - empty name`() {
        val rawRecord = ",2015-7-20,notes,914.96,INCREASED"

        val parsedRecord = parseStockRecord(rawRecord)

        assertNull(parsedRecord)
    }

    @Test
    fun `test invalid stock record - invalid date`() {
        val rawRecord = "KCT,2015-13-20,notes,914.96,INCREASED"

        val parsedRecord = parseStockRecord(rawRecord)

        assertNotNull(parsedRecord)
    }

    @Test
    fun `test invalid stock record - invalid value`() {
        val rawRecord = "KCT,2015-7-20,notes,invalid,INCREASED"

        val parsedRecord = parseStockRecord(rawRecord)

        assertNull(parsedRecord)
    }

    @Test
    fun `test invalid stock record - missing fields`() {
        val rawRecord = "KCT,2015-7-20,notes"

        val parsedRecord = parseStockRecord(rawRecord)

        assertNull(parsedRecord)
    }
}
