package com.route4me.printer.model

data class BTPrinter(
    val macAddress: String,
    val name: String,
    val manufacturer: PrinterManufacturer = PrinterManufacturer.TSC,
    val model: String = ""
)
