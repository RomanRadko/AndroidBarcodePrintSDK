package com.route4me.printer

import com.route4me.printer.model.SingletonHolder
import com.brother.ptouch.sdk.Printer
import com.brother.ptouch.sdk.PrinterInfo
import com.brother.ptouch.sdk.PrinterStatus
import android.bluetooth.BluetoothAdapter
import android.os.Environment

class BarPrinter(private val filePath: String) {

    companion object : SingletonHolder<BarPrinter, String>({
        BarPrinter(it)
    })

    fun print(): PrinterStatus {
        val externalStorageDir = Environment.getExternalStorageDirectory().toString()
        // define printer and printer setting information
        val printer = Printer()
        val printInfo = PrinterInfo()
        printInfo.printerModel = PrinterInfo.Model.RJ_3150
        printInfo.port = PrinterInfo.Port.BLUETOOTH
        //TODO: hardcoded values
        printInfo.customPaper = "$externalStorageDir/rj3150_76mm.bin"
        printInfo.macAddress = "00:11:EE:BB:AA:CC"
        printer.printerInfo = printInfo
        // Pass Bluetooth adapter to the library (Bluetooth only)
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        printer.setBluetooth(bluetoothAdapter)
        //print
        printer.startCommunication()
        val status = printer.printFile(filePath)
        printer.endCommunication()
        return status
    }

}