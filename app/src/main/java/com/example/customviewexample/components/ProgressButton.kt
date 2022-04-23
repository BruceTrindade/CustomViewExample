package com.example.customviewexample.components

import android.content.Context
import android.util.AttributeSet
import android.util.Size
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.customviewexample.R
import com.google.android.material.shape.MaterialShapeDrawable

class ProgressButton : ConstraintLayout {

    constructor(context: Context) : super(context) {
        initialComponent(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialComponent(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initialComponent(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initialComponent(attrs)
    }

    private fun initialComponent(attrs: AttributeSet?) {
        createRoundedCourners()
    }

    private fun createRoundedCourners(size: Size){

        val radiusResource = when (size) {
           // Size.SMALL -> R.dimen.size_radius_16_px
            //Size.MEDIUM -> R.dimen.size_radius_16_px
            //Size.LARGE -> R.dimen.size_radius_16_px
        }

        val radius = resources.getDimensionPixelSize(radiusResource)
        val color = ContextCompat.getColor(context, R.color.design_default_color_on_secondary)
        val text = TextView(context).apply {
            val buttonBackground = MaterialShapeDrawable().apply {
                setCornerSize(radius.toFloat())
                setTint(color)
            }
            background = buttonBackground
            width = 350
            height = 150
            text = "teste"

        }
        this.addView(text)

    }

}