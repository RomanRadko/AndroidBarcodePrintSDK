package com.route4me.printer.model

import com.route4me.printer.R

enum class PrinterManufacturer(val manufacturerName: String, val icon: Int) {
    BROTHER("Brother", R.drawable.ic_brother),
    ZEBRA("Zebra", R.drawable.ic_zebra),
    CITIZEN("Citizen", R.drawable.ic_citizen),
    TSC("TSC", R.drawable.ic_tsc),
    RONGTA("Rongta", R.drawable.ic_rongta)
}