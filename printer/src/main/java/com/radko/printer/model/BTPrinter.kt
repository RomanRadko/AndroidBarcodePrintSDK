package com.radko.printer.model

data class BTPrinter(
    val macAddress: String,
    val name: String,
    val manufacturer: PrinterManufacturer = PrinterManufacturer.TSC,
    val model: String = ""
)
