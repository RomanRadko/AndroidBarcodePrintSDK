package com.route4me.printer

import android.os.Looper
import com.route4me.printer.model.SingletonHolder
import com.zebra.sdk.comm.BluetoothConnectionInsecure
import com.zebra.sdk.comm.Connection


class ZebraPrinter (private val barcodeStr: String) {

    companion object : SingletonHolder<ZebraPrinter, String>({
        ZebraPrinter(it)
    })

    fun print() {
        try { // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
            val thePrinterConn: Connection = BluetoothConnectionInsecure("F4:CB:52:58:84:AE")
            // Initialize
            Looper.prepare()
            // Open the connection - physical connection is established here.
            thePrinterConn.open()
            // This example prints barcode near the top of the label.
            val zplData = "^FD$barcodeStr^FS"
            // Send the data to printer as a byte array.
            thePrinterConn.write(zplData.toByteArray())
            // Make sure the data got to the printer before closing the connection
            Thread.sleep(500)
            // Close the insecure connection to release resources.
            thePrinterConn.close()
            Looper.myLooper()!!.quit()
        } catch (e: Exception) { // Handle communications error here.
            e.printStackTrace()
        }
    }

}