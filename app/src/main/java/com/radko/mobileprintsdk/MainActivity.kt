package com.radko.mobileprintsdk

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.radko.PrintFragment


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToPrint()
    }

    private fun navigateToPrint() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val printFragment: Fragment = PrintFragment.newInstance()
        printFragment.arguments = Bundle().apply {
            putString(PrintFragment.BARCODE_KEY, "2019040101")
            putBoolean(PrintFragment.SHOULD_DRAW_LOGO_KEY, true)
        }
        transaction.replace(R.id.container, printFragment)
        transaction.commit()
    }
}
