package com.route4me.printer.model.status

import com.brother.ptouch.sdk.PrinterInfo

class BrotherStatus(private val error: PrinterInfo.ErrorCode) : PrinterStatus {

    override fun getMessage(): String {
        return when (error) {
            PrinterInfo.ErrorCode.ERROR_NONE -> "No errors"
            PrinterInfo.ErrorCode.ERROR_NOT_SAME_MODEL -> "Found a different printer model than expected"
            PrinterInfo.ErrorCode.ERROR_BROTHER_PRINTER_NOT_FOUND -> "Cannot find a Brother printer"
            PrinterInfo.ErrorCode.ERROR_PAPER_EMPTY -> "Paper empty"
            PrinterInfo.ErrorCode.ERROR_BATTERY_EMPTY -> "Battery weak"
            PrinterInfo.ErrorCode.ERROR_COMMUNICATION_ERROR -> "Failed to retrieve printer status"
            PrinterInfo.ErrorCode.ERROR_OVERHEAT -> "Print-head overheated"
            PrinterInfo.ErrorCode.ERROR_PAPER_JAM -> "Paper jam"
            PrinterInfo.ErrorCode.ERROR_HIGH_VOLTAGE_ADAPTER -> "High-voltage adapter"
            PrinterInfo.ErrorCode.ERROR_CHANGE_CASSETTE -> "Paper cassette change while printing"
            PrinterInfo.ErrorCode.ERROR_FEED_OR_CASSETTE_EMPTY -> "Feed error or paper cassette empty"
            PrinterInfo.ErrorCode.ERROR_SYSTEM_ERROR -> "System error"
            PrinterInfo.ErrorCode.ERROR_NO_CASSETTE -> "No paper-cassette"
            PrinterInfo.ErrorCode.ERROR_WRONG_CASSETTE_DIRECT -> "Paper-cassette loaded incorrectly"
            PrinterInfo.ErrorCode.ERROR_CREATE_SOCKET_FAILED -> "Failed to create socket"
            PrinterInfo.ErrorCode.ERROR_CONNECT_SOCKET_FAILED -> "Failed to connect *1"
            PrinterInfo.ErrorCode.ERROR_GET_OUTPUT_STREAM_FAILED -> "Failed to open output stream"
            PrinterInfo.ErrorCode.ERROR_GET_INPUT_STREAM_FAILED -> "Failed to open input stream"
            PrinterInfo.ErrorCode.ERROR_CLOSE_SOCKET_FAILED -> "Failed to close socket"
            PrinterInfo.ErrorCode.ERROR_OUT_OF_MEMORY -> "Insufficient memory *2"
            PrinterInfo.ErrorCode.ERROR_SET_OVER_MARGIN -> "Image size exceeds margin setting *3"
            PrinterInfo.ErrorCode.ERROR_NO_SD_CARD -> "No SD card"
            PrinterInfo.ErrorCode.ERROR_FILE_NOT_SUPPORTED -> "Unsupported file type"
            PrinterInfo.ErrorCode.ERROR_EVALUATION_TIMEUP -> "PDF library trial period expired"
            PrinterInfo.ErrorCode.ERROR_WRONG_CUSTOM_INFO -> "Wrong information in custom paper setting file"
            PrinterInfo.ErrorCode.ERROR_NO_ADDRESS -> "IP and/or MAC address not set"
            PrinterInfo.ErrorCode.ERROR_NOT_MATCH_ADDRESS -> "No printer found with the expected IP and/or MAC address"
            PrinterInfo.ErrorCode.ERROR_FILE_NOT_FOUND -> "File does not exist"
            PrinterInfo.ErrorCode.ERROR_TEMPLATE_FILE_NOT_MATCH_MODEL -> "Printer model in template file does not match selected printer"
            PrinterInfo.ErrorCode.ERROR_TEMPLATE_NOT_TRANS_MODEL -> "Selected printer model does not support Template transfer"
            PrinterInfo.ErrorCode.ERROR_COVER_OPEN -> "Cover is open (RJ/TD/PT-E550W)"
            PrinterInfo.ErrorCode.ERROR_WRONG_LABEL -> "Wrong media type"
            PrinterInfo.ErrorCode.ERROR_PORT_NOT_SUPPORTED -> "Unsupported interface © 2012-2016 Brother Industries, Ltd. All Rights Reserved. 41"
            PrinterInfo.ErrorCode.ERROR_WRONG_TEMPLATE_KEY -> "No file exists with the specified template key"
            PrinterInfo.ErrorCode.ERROR_BUSY -> "Busy (PT series/RJ-3xxx/TD-4xxx)"
            PrinterInfo.ErrorCode.ERROR_TEMPLATE_NOT_PRINT_MODEL -> "Selected printer model does not support Template printing"
            PrinterInfo.ErrorCode.ERROR_CANCEL -> "Printing has been cancelled"
            PrinterInfo.ErrorCode.ERROR_PRINTER_SETTING_NOT_SUPPORTED -> "Selected printer does not support device setting"
            PrinterInfo.ErrorCode.ERROR_INVALID_PARAMETER -> "Invalid parameter value"
            PrinterInfo.ErrorCode.ERROR_INTERNAL_ERROR -> "Internal error"
            PrinterInfo.ErrorCode.ERROR_TEMPLATE_NOT_CONTROL_MODEL -> "Print model does not support template list or remove operation"
            PrinterInfo.ErrorCode.ERROR_TEMPLATE_NOT_EXIST -> "Template not found in selected printer"
            PrinterInfo.ErrorCode.ERROR_BUFFER_FULL -> "Buffer full"
            PrinterInfo.ErrorCode.ERROR_TUBE_EMPTY -> "Tube empty"
            PrinterInfo.ErrorCode.ERROR_TUBE_RIBBON_EMPTY -> "Tube ribbon empty"
            PrinterInfo.ErrorCode.ERROR_UPDATE_FRIM_NOT_SUPPORTED -> TODO()
            PrinterInfo.ErrorCode.ERROR_OS_VERSION_NOT_SUPPORTED -> "Unsupported OS version"
            PrinterInfo.ErrorCode.ERROR_RESOLUTION_MODE -> "Error: High-resolution / draft printing error\n" +
                    "You can not use the high-resolution /\n" +
                    "high-speed printing. Change the print\n" +
                    "settings to a standard or use the AC adapter."
            PrinterInfo.ErrorCode.ERROR_POWER_CABLE_UNPLUGGING -> "Error: AC adapter or disconnect error\n" +
                    "Do not disconnect and reconnect the AC\n" +
                    "adapter during printing."
            PrinterInfo.ErrorCode.ERROR_BATTERY_TROUBLE -> "Error: battery error\n" +
                    "Replace to a new battery or use a proper AC\n" +
                    "adapter."
            PrinterInfo.ErrorCode.ERROR_UNSUPPORTED_MEDIA -> "Error: unsupported media error\n" +
                    "To continue printing, attach a corresponding\n" +
                    "cassette properly."
            PrinterInfo.ErrorCode.ERROR_TUBE_CUTTER -> "Error: tube cutter error\n" +
                    "Tube cutter of the P-touch does not work."
            PrinterInfo.ErrorCode.ERROR_UNSUPPORTED_TWO_COLOR -> "Error: monochromatic medium error\n" +
                    "Paper set inside is not compatible with\n" +
                    "two-color printing."
            PrinterInfo.ErrorCode.ERROR_UNSUPPORTED_MONO_COLOR -> "Error: 2-color medium error\n" +
                    "Paper set inside is not compatible with\n" +
                    "monochrome printing"
            PrinterInfo.ErrorCode.ERROR_MINIMUM_LENGTH_LIMIT -> TODO()
        }
    }

}