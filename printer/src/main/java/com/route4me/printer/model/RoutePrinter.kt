package com.route4me.printer.model

interface RoutePrinter {
    fun print(barcode: String, macAddress: String): PrintStatus
}