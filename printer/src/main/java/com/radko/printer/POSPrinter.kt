package com.radko.printer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.radko.printer.image.POSImagePrinter
import com.radko.printer.image.POSPrinterCommands.ADD_GAP
import com.radko.printer.image.POSPrinterCommands.CENTER_TEXT
import com.radko.printer.image.POSPrinterCommands.PRINT_BARCODE_39
import com.radko.printer.image.POSPrinterCommands.SET_DIGITS_POSITION_BELOW_CODE
import com.radko.printer.image.POSPrinterCommands.SET_TEXT_2X_HEIGHT
import com.radko.printer.image.POSPrinterCommands.SET_TEXT_2X_WIDTH
import com.radko.printer.model.PrintStatus
import com.radko.printer.model.RoutePrinter
import com.radko.printer.model.SingletonHolder
import com.radko.printer.model.prefs.PrintPreferences
import com.radko.printer.model.status.TSCStatus
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class POSPrinter(private val context: Context) : RoutePrinter {

    companion object : SingletonHolder<POSPrinter, Context>({
        POSPrinter(it)
    })

    private val MY_UUID =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val TAG = "POSPrinter"

    private var isConnected = false
    private var btSocket: BluetoothSocket? = null
    private var outStream: OutputStream? = null
    private var inStream: InputStream? = null
    private val readBuf = ByteArray(1024)

    @ExperimentalStdlibApi
    override fun print(barcode: String, macAddress: String): PrintStatus {
        var status: Boolean = openPort(macAddress)
        if (status) {
            status = printBarcode(barcode)
            if (PrintPreferences.getInstance(context).drawLogo!!) {
                POSImagePrinter().printImage(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.logo
                    ), outStream
                )
            }
            write(ADD_GAP)
        } else {
            return PrintStatus(status, "Failed to open BT port.")
        }
        val statusMessage: String = getPrinterStatus().getMessage()
        status = closePort()
        return PrintStatus(status, statusMessage)
    }

    private fun openPort(address: String?): Boolean {
        val device: BluetoothDevice?
        val mBluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        return if (mBluetoothAdapter!!.isEnabled) {
            isConnected = true
            device = mBluetoothAdapter.getRemoteDevice(address)
            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
            } catch (exception: IOException) {
                Log.e(TAG, "IOException while trying to create socket.", exception)
                return false
            }
            mBluetoothAdapter.cancelDiscovery()
            try {
                (btSocket as BluetoothSocket).connect()
                outStream = (btSocket as BluetoothSocket).outputStream
                inStream = (btSocket as BluetoothSocket).inputStream
            } catch (exception: IOException) {
                Log.e(TAG, "IOException while trying to connect socket.", exception)
                return false
            }
            Thread.sleep(1000L)
            true
        } else {
            Log.e(TAG, "Bluetooth not Enabled.")
            isConnected = false
            false
        }
    }

    private fun getPrinterStatus(): TSCStatus {
        val message = byteArrayOf(27, 33, 63)
        return if (outStream != null && inStream != null) {
            try {
                outStream!!.write(message)
            } catch (exception: IOException) {
                Log.e(TAG, "IOException while trying to write output stream.", exception)
                return TSCStatus(128)
            }
            Thread.sleep(1000L)
            try {
                while (inStream!!.available() > 0) {
                    inStream!!.read(readBuf)
                }
            } catch (exception: IOException) {
                Log.e(TAG, "IOException while trying to read the input stream.", exception)
                return TSCStatus(128)
            }
            TSCStatus(readBuf[0].toInt())
        } else {
            TSCStatus(128)
        }
    }

    private fun closePort(): Boolean {
        Thread.sleep(1000L)
        return if (btSocket!!.isConnected) {
            try {
                isConnected = false
                btSocket!!.close()
            } catch (exception: IOException) {
                Log.e(TAG, "IOException while trying to close the port.", exception)
                return false
            }
            Thread.sleep(1000L)
            true
        } else {
            false
        }
    }

    @ExperimentalStdlibApi
    //https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=128
    private fun printBarcode(barcode: String): Boolean {
        val contents: ByteArray = barcode.encodeToByteArray()
        // include the content length after the mode selector (0x49)
        val commands = SET_TEXT_2X_HEIGHT + SET_TEXT_2X_WIDTH +
                CENTER_TEXT + SET_DIGITS_POSITION_BELOW_CODE + PRINT_BARCODE_39 + barcode.length.toByte()

        val bytes = ByteArray(commands.size + contents.size)
        System.arraycopy(commands, 0, bytes, 0, commands.size)
        System.arraycopy(contents, 0, bytes, commands.size, contents.size)
        return write(bytes)
    }

    @ExperimentalStdlibApi
    private fun write(command: ByteArray): Boolean {
        return if (outStream != null && inStream != null) {
            try {
                outStream!!.write(command)
                true
            } catch (exception: IOException) {
                Log.e(TAG, "IOException while trying to write print command.", exception)
                false
            }
        } else {
            false
        }
    }


}