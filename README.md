# Barcode Print SDK
This module is aimed for printing barcode on the next mobile printers:

[Brother](https://mariusbelin.files.wordpress.com/2017/02/brother-print-sdk-for-android-manual.pdf)

[Zebra (ZPL format)](https://www.zebra.com/content/dam/zebra_new_ia/en-us/)

[Citizen](https://www.b4x.com/android/forum/attachments/android-escpos_-program-manual-pdf.21181/)

[TSC and Rongta (and any with POS support)](https://www.b4x.com/android/forum/attachments/android-escpos_-program-manual-pdf.21181/)

<p align="center">
<img src ="https://user-images.githubusercontent.com/4493267/77412195-39450c00-6dc6-11ea-95e4-4ef8f3be9176.jpg" width="300" height="500">
</p>

<p align="center">
<img src="https://user-images.githubusercontent.com/4493267/77412336-6abdd780-6dc6-11ea-90e7-5811d0d39ec4.png" width="200" height="400">
<img src="https://user-images.githubusercontent.com/4493267/77412527-a9ec2880-6dc6-11ea-9cd4-c9524226863d.png" width="200" height="400">
<img src="https://user-images.githubusercontent.com/4493267/77412574-ba9c9e80-6dc6-11ea-88f4-3ce24d07e264.png" width="200" height="400">
<img src="https://user-images.githubusercontent.com/4493267/77412420-87f2a600-6dc6-11ea-8a76-e04392028e84.png" width="200" height="400">
</p>

**Preconditions:**
Bluetooth Printer should be paired with the mobile device.

**Usage:**
To get lib .aar file built, need to navigate to Gradle panel in Android Studio, choose:
*routegraph* -> *Tasks* -> *build* -> *assemble*

As an input barcode value and print logo config arguments should be provided for `PrintFragment` which is androidx.fragment.app.Fragment:

Example:

```kotlin
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
```
