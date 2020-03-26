package com.radko.printer.model.status

import com.citizen.sdk.ESCPOSConst

class CitizenStatus(private val code: Int) : PrinterStatus {

    override fun getMessage(): String {
        return when (code) {
            ESCPOSConst.CMP_SUCCESS -> "The operation is success."
            ESCPOSConst.CMP_E_CONNECTED -> "The printer is already connected."
            ESCPOSConst.CMP_E_DISCONNECT -> "The printer is not connected."
            ESCPOSConst.CMP_E_CONNECT_NOTFOUND -> "Failed to check the support model after connecting to the device."
            ESCPOSConst.CMP_E_CONNECT_OFFLINE -> "Failed to check the printer status after connecting to the device."
            ESCPOSConst.CMP_E_NOCONTEXT -> "The context is not specified."
            ESCPOSConst.CMP_E_BT_DISABLE -> "The setting of the Bluetooth device is invalid."
            ESCPOSConst.CMP_E_BT_NODEVICE -> "The Bluetooth device is not found."
            ESCPOSConst.CMP_E_ILLEGAL -> " Unsupported operation with the Device, or an invalid parameter value was used."
            ESCPOSConst.CMP_E_OFFLINE -> "The printer is off-line."
            ESCPOSConst.CMP_E_NOEXIST -> "The file name does not exist."
            ESCPOSConst.CMP_E_FAILURE -> "The Service cannot perform the requested procedure."
            ESCPOSConst.CMP_E_NO_LIST -> "The printer cannot be found in the printer search."
            ESCPOSConst.CMP_EPTR_COVER_OPEN -> "The cover of the printer opens."
            ESCPOSConst.CMP_EPTR_REC_EMPTY -> "The printer is out of paper."
            ESCPOSConst.CMP_EPTR_BADFORMAT -> "The specified file is in an unsupported format."
            ESCPOSConst.CMP_EPTR_TOOBIG -> "The specified bitmap is either too big."
            else -> "Status code: $code not found."
        }
    }
}