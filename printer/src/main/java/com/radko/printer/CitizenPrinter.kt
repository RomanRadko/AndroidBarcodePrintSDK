package com.radko.printer

import com.citizen.sdk.ESCPOSConst
import com.citizen.sdk.ESCPOSPrinter
import com.radko.printer.model.PrintStatus
import com.radko.printer.model.RoutePrinter
import com.radko.printer.model.status.CitizenStatus

class CitizenPrinter private constructor() : RoutePrinter {

    companion object {
        @Volatile
        private var instance: CitizenPrinter? = null

        fun getInstance(): CitizenPrinter =
            instance ?: synchronized(this) {
                instance ?: CitizenPrinter().also { instance = it }
            }
    }

    override fun print(barcode: String, macAddress: String): PrintStatus {
        val posPtr = ESCPOSPrinter()
        // Connect
        var result: Int = posPtr.connect(
            ESCPOSConst.CMP_PORT_Bluetooth_Insecure,
            macAddress
        )
        if (ESCPOSConst.CMP_SUCCESS == result) {
            posPtr.setEncoding("UTF-8")
            // Start Transaction ( Batch )
            posPtr.transactionPrint(ESCPOSConst.CMP_TP_TRANSACTION)
            posPtr.printBarCode(
                barcode,
                ESCPOSConst.CMP_BCS_UPCA,
                64,
                4,
                ESCPOSConst.CMP_ALIGNMENT_LEFT,
                ESCPOSConst.CMP_HRI_TEXT_BELOW
            )
            // Partial Cut with Pre-Feed
            posPtr.cutPaper(ESCPOSConst.CMP_CUT_PARTIAL_PREFEED)
            // End Transaction ( Batch )
            result = posPtr.transactionPrint(ESCPOSConst.CMP_TP_NORMAL)
            // Disconnect
            posPtr.disconnect()
            return PrintStatus(ESCPOSConst.CMP_SUCCESS != result, CitizenStatus(result).getMessage())
        } else { // Connect Error
            return PrintStatus(false, "BT connection was failed.")
        }
    }
}