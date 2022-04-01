package com.example.customviewexample

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.customviewexample.databinding.ProgressButtonBinding

class ButtonProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

        private var title: String? = null
        private var loadingTitle: String? = null

        private val viewBinding = ProgressButtonBinding
            .inflate(LayoutInflater.from(context), this, true)

        init {
            setLayout(attrs)
        }

        private fun setLayout( attrs: AttributeSet?){
            attrs.let {
                val attributes = context.obtainStyledAttributes(
                    it,
                    R.styleable.ButtonProgress
                )

                setBackgroundResource(R.drawable.progress_button)

                val titleResId = attributes.getResourceId(R.styleable.ButtonProgress_progress_button_title)

                attributes.recycle()
            }
        }

}