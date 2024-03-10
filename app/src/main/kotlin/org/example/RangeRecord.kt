package org.example

data class RangeRecord(
    val name: String,
    var firstRecord: StockRecord,
    var lastRecord: StockRecord
)

operator fun RangeRecord.plusAssign(record: StockRecord) {
    if (record.date.before(this.firstRecord.date)) {
        this.firstRecord = record
    }
    if (record.date.after(this.lastRecord.date)) {
        this.lastRecord = record
    }
}

operator fun RangeRecord.plusAssign(rangeRecord: RangeRecord) {
    if (rangeRecord.firstRecord.date.before(this.firstRecord.date)) {
        this.firstRecord = rangeRecord.firstRecord
    }
    if (rangeRecord.lastRecord.date.after(this.lastRecord.date)) {
        this.lastRecord = rangeRecord.lastRecord
    }
}

operator fun RangeRecord.plus(rangeRecord: RangeRecord): RangeRecord {
    if (rangeRecord.firstRecord.date.before(this.firstRecord.date)) {
        this.firstRecord = rangeRecord.firstRecord
    }
    if (rangeRecord.lastRecord.date.after(this.lastRecord.date)) {
        this.lastRecord = rangeRecord.lastRecord
    }
    return this
}