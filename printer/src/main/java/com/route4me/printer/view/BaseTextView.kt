package com.route4me.printer.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class BaseTextView : TextView {
    private var attrs: AttributeSet? = null
    private var defStyle = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        this.attrs = attrs
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        this.attrs = attrs
        this.defStyle = defStyle
        init()
    }

    private fun init() {
        this.typeface = Typeface.createFromAsset(context.assets, "fonts/OpenSansRegular.ttf")
    }

    override fun setTypeface(tf: Typeface?, style: Int) {
        super.setTypeface(Typeface.createFromAsset(context.assets, "fonts/OpenSansRegular.ttf"), style)
    }

    override fun setTypeface(tf: Typeface?) {
        super.setTypeface(Typeface.createFromAsset(context.assets, "fonts/OpenSansRegular.ttf"))
    }
}