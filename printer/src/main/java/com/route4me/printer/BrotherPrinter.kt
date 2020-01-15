package com.route4me.printer

import android.bluetooth.BluetoothAdapter
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.brother.ptouch.sdk.Printer
import com.brother.ptouch.sdk.PrinterInfo
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.route4me.printer.model.SingletonHolder

private const val TAG = "BrotherPrinter"
class BrotherPrinter(private val barcodeValue: String) {

    companion object : SingletonHolder<BrotherPrinter, String>({
        BrotherPrinter(it)
    })

    fun print(macAddress: String): Boolean {
        val externalStorageDir = Environment.getExternalStorageDirectory().toString()
        // define printer and printer setting information
        val printer = Printer().apply {
            printerInfo = PrinterInfo().apply {
                printerModel = PrinterInfo.Model.RJ_3150
                port = PrinterInfo.Port.BLUETOOTH
                customPaper = "$externalStorageDir/Download/rj3150_76mm.bin"
                this.macAddress = macAddress
            }
        }

        // Pass Bluetooth adapter to the library (Bluetooth only)
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        printer.setBluetooth(bluetoothAdapter)

        try {
            //print
            printer.startCommunication()
            // Brother SDK accepts only images files, so barcode to image conversion should be performed
            val bitmap = generateBarcodeBitmap(barcodeValue) ?: return false
            val status = printer.printImage(bitmap)
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

}