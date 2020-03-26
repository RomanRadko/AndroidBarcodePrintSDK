package com.radko.printer.image

import android.graphics.Bitmap
import com.radko.printer.image.POSPrinterCommands.SELECT_BIT_IMAGE_MODE
import com.radko.printer.image.POSPrinterCommands.SET_LINE_SPACING_24
import com.radko.printer.image.POSPrinterCommands.SET_LINE_SPACING_30
import java.io.OutputStream
import kotlin.experimental.or


class POSImagePrinter {

    fun printImage(imageBitmap: Bitmap, outStream: OutputStream?) {
        val pixels = getPixelsSlow(imageBitmap)
        // Set the line spacing at 24 (we'll print 24 dots high)
        outStream!!.write(SET_LINE_SPACING_24)
        var y = 0
        while (y < pixels!!.size) {

            // Like I said before, when done sending data,
            // the printer will resume to normal text printing
            outStream.write(SELECT_BIT_IMAGE_MODE)
            // Set nL and nH based on the width of the image
            outStream.write(
                byteArrayOf(
                    (0x00ff and pixels[y].size).toByte()
                    , (0xff00 and pixels[y].size shr 8).toByte()
                )
            )
            for (x in pixels[y].indices) {
                // for each stripe, recollect 3 bytes (3 bytes = 24 bits)
                outStream.write(recollectSlice(y, x, pixels))
            }

            // Do a line feed, if not the printing will resume on the same line
            outStream.write(POSPrinterCommands.FEED_LINE)
            y += 24
        }
        outStream.write(SET_LINE_SPACING_30)
    }

    // The performance of this method
    // is rather poor, place for improvement
    private fun getPixelsSlow(image: Bitmap): Array<IntArray>? {
        val width: Int = image.width
        val height: Int = image.height
        val result =
            Array(height) { IntArray(width) }
        for (row in 0 until height) {
            for (col in 0 until width) {
                result[row][col] = image.getPixel(col, row)
            }
        }
        return result
    }

    private fun recollectSlice(
        y: Int,
        x: Int,
        img: Array<IntArray>
    ): ByteArray? {
        val slices = byteArrayOf(0, 0, 0)
        var yy = y
        var i = 0
        while (yy < y + 24 && i < 3) {
            var slice: Byte = 0
            for (b in 0..7) {
                val yyy = yy + b
                if (yyy >= img.size) {
                    continue
                }
                val col = img[yyy][x]
                val v = shouldPrintColor(col)
                slice = slice or ((if (v) 1 else 0) shl 7 - b).toByte()
            }
            slices[i] = slice
            yy += 8
            i++
        }
        return slices
    }

    private fun shouldPrintColor(col: Int): Boolean {
        val threshold = 127
        val luminance: Int
        val a: Int = col shr 24 and 0xff
        if (a != 0xff) { // Ignore transparencies
            return false
        }
        val r: Int = col shr 16 and 0xff
        val g: Int = col shr 8 and 0xff
        val b: Int = col and 0xff
        luminance = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
        return luminance < threshold
    }
}