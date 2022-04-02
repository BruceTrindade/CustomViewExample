package com.example.customviewexample

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.customviewexample.databinding.ProgressButtonBinding

class ButtonProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var title: String? = null
    private var loadingTitle: String? = null

    private val binding = ProgressButtonBinding
        .inflate(LayoutInflater.from(context), this, true)

    private var state: ProgressButtonState = ProgressButtonState.Normal
        set(value) {
            field = value
            refreshState()
        }

    init {
        setLayout(attrs)
        refreshState()
    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs.let {
            val attributes = context.obtainStyledAttributes(
                it,
                R.styleable.ButtonProgress
            )

            setBackgroundResource(R.drawable.progress_button)

            val titleResId =
                attributes.getResourceId(R.styleable.ButtonProgress_progress_button_title, 0)
            if (titleResId != 0) {
                title = context.getString(titleResId)
            }

            val loadingTitleResId = attributes.getResourceId(
                R.styleable.ButtonProgress_progress_button_loading_title,
                0
            )
            if (loadingTitleResId != 0) {
                loadingTitle = context.getString(loadingTitleResId)
            }

            attributes.recycle()
        }
    }


    private fun refreshState() {
        isEnabled = state.isEnable
        isClickable = state.isEnable
        refreshDrawableState()

        binding.textTitle.run {
            isEnabled = state.isEnable
            isClickable = state.isEnable

        }

        binding.progressBar.visibility = state.progressVisibility

        when(state) {
            ProgressButtonState.Normal -> binding.textTitle.text = title
            ProgressButtonState.Loading -> binding.textTitle.text = loadingTitle
        }
    }

    fun setLoading() {
        state = ProgressButtonState.Loading
    }

    fun setNormal() {
        state = ProgressButtonState.Normal
    }

    sealed class ProgressButtonState(val isEnable: Boolean, val progressVisibility: Int){
        object Normal : ProgressButtonState(true, View.GONE)
        object Loading : ProgressButtonState(false, View.VISIBLE)
    }

}