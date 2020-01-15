package com.route4me.printer

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Log
import com.brother.ptouch.sdk.Printer
import com.brother.ptouch.sdk.PrinterInfo
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.route4me.printer.model.SingletonHolder
import java.io.*
import java.util.*


private const val TAG = "BrotherPrinter"

class BrotherPrinter(private val barcodeValue: String) {

    companion object : SingletonHolder<BrotherPrinter, String>({
        BrotherPrinter(it)
    })

    fun print(context: Context, macAddress: String): Boolean {
        var paperInfoFile: File
        // define printer and printer setting information
        val printer = Printer().apply {
            printerInfo = PrinterInfo().apply {
                printerModel = PrinterInfo.Model.RJ_3150
                port = PrinterInfo.Port.BLUETOOTH
                val wrapper = ContextWrapper(context)
                paperInfoFile = wrapper.getDir("PaperData", Context.MODE_PRIVATE)
                paperInfoFile = File(paperInfoFile, "${UUID.randomUUID()}.bin")
                val inputStream: InputStream = context.resources.openRawResource(
                    context.resources.getIdentifier(
                        "rj3150_76mm",
                        "raw", context.packageName
                    )
                )
                copyInputStreamToFile(inputStream, paperInfoFile)
                customPaper = paperInfoFile.absolutePath
                this.macAddress = macAddress
            }
        }

        // Pass Bluetooth adapter to the library (Bluetooth only)
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        printer.setBluetooth(bluetoothAdapter)

        try {
            //print
            printer.startCommunication()
            // Brother SDK accepts bitmap as input so barcode to bitmap conversion should be performed
            val bitmap = generateBarcodeBitmap(barcodeValue) ?: return false
            val status = printer.printImage(bitmap)
            paperInfoFile.delete()
            return status.errorCode == PrinterInfo.ErrorCode.ERROR_NONE
        } finally {
            printer.endCommunication()
        }
    }

    private fun generateBarcodeBitmap(input: String): Bitmap? {
        return try {
            MultiFormatWriter().encode(input, BarcodeFormat.CODABAR, 800, 200)
                .let(BarcodeEncoder()::createBitmap)
        } catch (e: WriterException) {
            Log.e(TAG, "Error has been occurred while trying to generate barcode bitmap.", e)
            null
        }
    }

    // Copy an InputStream to a File.
    private fun copyInputStreamToFile(`in`: InputStream, file: File): Boolean {
        var out: OutputStream? = null
        try {
            out = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (`in`.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error has been occurred while trying to copy input stream to file.", e)
            return false
        } finally { // Ensure that the InputStreams are closed even if there's an exception.
            try {
                out?.close()
                `in`.close()
            } catch (e: IOException) {
                Log.e(TAG, "Error has been occurred while trying to close input stream.", e)
            }
        }
        return true
    }

}