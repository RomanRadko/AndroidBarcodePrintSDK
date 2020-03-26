package com.radko.printer.model.status

class TSCStatus(private val code: Int) : PrinterStatus {

    override fun getMessage(): String {
        return when (code) {
            0 -> "Normal"
            1 -> "Head opened"
            2 -> "Paper Jam"
            3 -> "Paper Jam and head opened"
            4 -> "Out of paper"
            5 -> "Out of paper and head opened"
            8 -> "Out of ribbon"
            9 -> "Out of ribbon and head opened"
            10 -> "Out of ribbon and paper jam"
            11 -> "Out of ribbon, paper jam and head opened"
            12 -> "Out of ribbon and out of paper"
            13 -> "Out of ribbon, out of paper and head opened"
            16 -> "Pause"
            32 -> "Printing"
            128 -> "Other error"
            100 -> "Not Connected"
            else -> "Unknown"
        }
    }
}