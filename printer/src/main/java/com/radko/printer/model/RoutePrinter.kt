package com.radko.printer.model

interface RoutePrinter {
    fun print(barcode: String, macAddress: String): PrintStatus
}