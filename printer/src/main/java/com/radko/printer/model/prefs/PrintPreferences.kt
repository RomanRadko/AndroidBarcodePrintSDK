package com.radko.printer.model.prefs

import android.content.Context
import com.radko.printer.model.SingletonHolder

const val NAME: String = "GraphPreferences"

class PrintPreferences(context: Context) : Preferences(context, NAME) {

    companion object : SingletonHolder<PrintPreferences, Context>({
        PrintPreferences(it)
    })

    var deviceName by stringPref()
    var macAddress by stringPref()
    var manufacturer by stringPref()
    //todo: need to add selection dialog for a model, needed to Brother and Citizen manufacturers
    var model by stringPref()
    var barcode by stringPref()
    var drawLogo by booleanPref()
}