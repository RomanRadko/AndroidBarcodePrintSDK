package com.radko.printer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.radko.printer.model.PrintStatus
import com.radko.printer.model.RoutePrinter
import com.radko.printer.model.status.TSCStatus
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class TSCPrinter private constructor() : RoutePrinter {

    companion object {
        @Volatile
        private var instance: TSCPrinter? = null

        fun getInstance(): TSCPrinter =
            instance ?: synchronized(this) {
                instance ?: TSCPrinter().also { instance = it }
            }

        private val MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private val TAG = "TSCPrinter"

    }

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
            addGap()
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
        val formats = byteArrayOf(
            //set 2x height
            0x1b,
            0x21,
            0x10,
            //set 2x width
            0x1b,
            0x21,
            0x20,
            //center text
            0x1b,
            0x61,
            0x01,
            //digits position
            0x1d,
            0x48,
            0x02,
            //print
            0x1d,
            0x6b,
            0x48,//CODE93

            barcode.length.toByte()
        )

        val bytes = ByteArray(formats.size + contents.size)
        System.arraycopy(formats, 0, bytes, 0, formats.size)
        System.arraycopy(contents, 0, bytes, formats.size, contents.size)
        return write(bytes)
    }


    @ExperimentalStdlibApi
    private fun addGap(): Boolean {
        val command = byteArrayOf(
            //add a gap after print for better paper cut of experience
            0x1b,
            0x4a,
            0x50
        )
        return write(command)
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