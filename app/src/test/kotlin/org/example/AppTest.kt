/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package org.example

import kotlin.test.Test

import kotlinx.coroutines.runBlocking
import org.mockito.Mockito.*

class AppTest {

    @Test
    fun testMain() = runBlocking {
        // Mock input file content
        val mockFileContent = """
Name,Date,Notes,Value,Change
IQZ,2015-7-8,notes,656.36,INCREASED
DLV,2015-8-8,notes,173.35,INCREASED
DLV,2015-10-4,notes,231.67,INCREASEL
DLV,2015-9-7,notes,209.57,DECREASED
IQZ,2015-9-7,notes,641.23,DECREASED
IQZ,2015-10-4,notes,657.32,INCREASED
DLV,2015-8-18,notes,233.43,INCREASED
DLV,2015-9-15,notes,158.73,DECREASED
IQZ,2015-10-8,notes,537.53,DECREASED
IQZ,2015-10-6,notes,Invalid,UNKNOWN
        """.trimIndent()

        // Mock the FileLoader
        val mockFileLoader = mock(FileLoader::class.java)

        // Mock DataProcessor
        val dataProcessor = DataProcessor()

        // Set up the FileLoader to return mocked dataProcessor
        `when`(mockFileLoader.processFile()).then {
            val lines = mockFileContent.lineSequence().asIterable()
            lines.forEach { line ->
                val record = parseStockRecord(line)
                if (record != null) {
                    dataProcessor.updateRecords(record)
                }
            }
        }

        // Call the main method
        mockFileLoader.processFile()

        // Verify the result
        val (maxIncreaseCompany, maxIncreaseValue) = dataProcessor.findLargestIncrease()
        assert(maxIncreaseCompany == "DLV")
        assert(Math.abs(maxIncreaseValue - 58.32) < 0.01)
    }



    @Test
    fun testMain2() = runBlocking {
        // Mock input file content
        val mockFileContent = """
Name,Date,Notes,Value,Change
IQZ,2015-7-8,notes,656.36,DECREASED
DLV,2015-8-8,notes,773,35,DECREASED
DLV,2015-10-4,notes,231.67,DECREASED
DLV,2015-9-7,notes,299,57,DECREASED
        """.trimIndent()

        // Mock the FileLoader
        val mockFileLoader = mock(FileLoader::class.java)

        // Mock DataProcessor
        val dataProcessor = DataProcessor()

        // Set up the FileLoader to return mocked dataProcessor
        `when`(mockFileLoader.processFile()).then {
            val lines = mockFileContent.lineSequence().asIterable()
            lines.forEach { line ->
                val record = parseStockRecord(line)
                if (record != null) {
                    dataProcessor.updateRecords(record)
                }
            }
        }

        // Call the main method
        mockFileLoader.processFile()

        // Verify the result
        val (maxIncreaseCompany, maxIncreaseValue) = dataProcessor.findLargestIncrease()
        assert(maxIncreaseCompany == "")
        assert(maxIncreaseValue == 0.0)
    }
}
