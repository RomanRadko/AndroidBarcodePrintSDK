package com.route4me.printer

import android.os.Looper
import android.util.Log
import com.route4me.printer.model.PrintStatus
import com.route4me.printer.model.RoutePrinter
import com.zebra.sdk.comm.BluetoothConnectionInsecure
import com.zebra.sdk.comm.Connection

private const val TAG = "ZebraPrinter"

class ZebraPrinter private constructor() : RoutePrinter {

    companion object {
        @Volatile
        private var instance: ZebraPrinter? = null

        fun getInstance(): ZebraPrinter =
            instance ?: synchronized(this) {
                instance ?: ZebraPrinter().also { instance = it }
            }
    }

    override fun print(barcode: String, macAddress: String): PrintStatus {
        try { // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
            val thePrinterConn: Connection = BluetoothConnectionInsecure(macAddress)
            // Initialize
            if (Looper.myLooper() == null) {
                Looper.prepare()
            }
            // Open the connection - physical connection is established here.
            thePrinterConn.open()
            // This example prints barcode near the top of the label.
            val zplData = "^FD$barcode^FS"
            // Send the data to printer as a byte array.
            thePrinterConn.write(zplData.toByteArray(charset("UTF-8")))
            // Make sure the data got to the printer before closing the connection
            Thread.sleep(500)
            // Close the insecure connection to release resources.
            thePrinterConn.close()
            Looper.myLooper()!!.quit()
            return PrintStatus(true, "")
        } catch (e: Exception) { // Handle communications error here.
            Log.e(TAG, "Error has been occurred while trying to print on Zebra.")
            return PrintStatus(false, "")
        }
    }

}