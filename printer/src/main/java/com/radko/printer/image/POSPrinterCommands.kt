package com.radko.printer.image

/**
 * Printer commands collection/enum.
 */
object POSPrinterCommands {
    var FEED_LINE = byteArrayOf(10)
    var SELECT_BIT_IMAGE_MODE = byteArrayOf(0x1b, 0x2a, 33)
    var SET_LINE_SPACING_24 = byteArrayOf(0x1b, 0x33, 24)
    var SET_LINE_SPACING_30 = byteArrayOf(0x1b, 0x33, 30)
    var SET_TEXT_2X_HEIGHT = byteArrayOf(0x1b, 0x21, 0x10)
    var SET_TEXT_2X_WIDTH = byteArrayOf(0x1b, 0x21, 0x20)
    var CENTER_TEXT = byteArrayOf(0x1b, 0x61, 0x01)
    var SET_DIGITS_POSITION_BELOW_CODE = byteArrayOf(0x1d, 0x48, 0x02)
    var PRINT_BARCODE_39 = byteArrayOf(0x1d, 0x6b, 0x48)
    var ADD_GAP = byteArrayOf(0x1b, 0x4a, 0x50)
}