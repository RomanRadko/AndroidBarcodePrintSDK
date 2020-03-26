package com.radko.printer

import android.os.Looper
import android.util.Log
import com.radko.printer.model.PrintStatus
import com.radko.printer.model.RoutePrinter
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
            /*
            * ^XA
              ^BY5,2,270
              ^FO100,50^BC^FD12345678^FS
              ^XZ
            * */
            val zplData = "^XA\n^BY5,2,270\n^FO100,50^BC^$barcode^FS\n^XZ"
            // Send the data to printer as a byte array.
            thePrinterConn.write(zplData.toByteArray(charset("UTF-8")))
            // Make sure the data got to the printer before closing the connection
            Thread.sleep(500)
            // Close the insecure connection to release resources.
            thePrinterConn.close()
            Looper.myLooper()!!.quit()
            return PrintStatus(true, "Print was successful.")
        } catch (exception: Exception) { // Handle communications error here.
            Log.e(TAG, "Error has been occurred while trying to print on Zebra.", exception)
            return PrintStatus(false, "Print was failed with error: ${exception.message}")
        }
    }

}