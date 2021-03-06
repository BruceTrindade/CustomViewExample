package com.example.customviewexample.components

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import com.example.customviewexample.R
import java.util.concurrent.TimeUnit

class HorizontalLoading @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var dotRadius: Int = 0
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var dotsNumbers: Int = 0
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var delayTime: Int = 0
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var color: Int = 0
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private val animation = mutableListOf<Animator>()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var primaryAnimation: ValueAnimator? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var dotHorizontalLoading: LinearLayout

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var background = R.drawable.ic_dot_loading

    private var duration: Long = 0L

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HorizontalLoading,
            0, 0
        ).apply {

            try {
                dotsNumbers = getInteger(R.styleable.HorizontalLoading_numberDots, 3)
                delayTime = getInteger(R.styleable.HorizontalLoading_fadeTime, 2)
                color = getColor(R.styleable.HorizontalLoading_color, getDefaultColor())
                val radiusAttr = getInteger(R.styleable.HorizontalLoading_radius, 3)

                dotRadius = convertDpToPixel(radiusAttr.toFloat(), context)
                duration = calculateDurationForEachView()
                dotHorizontalLoading = LinearLayout(context)
                val progressBarLayoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                progressBarLayoutParams.gravity = Gravity.CENTER
                dotHorizontalLoading.layoutParams = progressBarLayoutParams
                addView(dotHorizontalLoading)
                animation.clear()
                for (index in 0 until dotsNumbers) {
                    val dot = View(context)
                    composeParamsForDotView(context, dot)
                    dotHorizontalLoading.addView(dot)
                    val animator = createFadeAnimation(dot)
                    animation.add(animator)
                }
                animationProcedures()
            } finally {
                recycle()
            }
        }
    }

    private fun getDefaultColor(): Int {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            resources.getColor(R.color.teal_200, context.theme)
        } else {
            resources.getColor(R.color.teal_200)
        }
    }

    private fun createFadeAnimation(view: View): Animator {
        val animator = ValueAnimator.ofFloat(0.1f, 1.0f)
        animator.addUpdateListener {
            view.alpha = it.animatedValue as Float
        }
        animator.duration = duration
        animator.repeatCount = 1
        animator.repeatMode = ValueAnimator.REVERSE
        animator.interpolator = AccelerateDecelerateInterpolator()
        return animator
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun composeParamsForDotView(context: Context, dot: View) {
        val layoutParams = LayoutParams(dotRadius * 2, dotRadius * 2)
        layoutParams.rightMargin = convertDpToPixel(4f, context)
        dot.layoutParams = layoutParams
        val icDot = context.getDrawable(background)
        icDot?.let {
            setDrawableColor(it, color)
        }
        dot.background = icDot
    }

    private fun setDrawableColor(drawable: Drawable, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun animationProcedures() {
        primaryAnimation?.cancel()
        primaryAnimation = ValueAnimator.ofInt(0, dotsNumbers)
        primaryAnimation?.addUpdateListener {
            if (it.animatedValue != dotsNumbers)
                animation[it.animatedValue as Int].start()
        }
        primaryAnimation?.repeatMode = ValueAnimator.RESTART
        primaryAnimation?.repeatCount = ValueAnimator.INFINITE
        primaryAnimation?.duration = durationInMilli()
        primaryAnimation?.interpolator = LinearInterpolator()
    }

    private fun durationInMilli() =
        TimeUnit.MILLISECONDS.convert(delayTime.toLong(), TimeUnit.SECONDS)

    private fun calculateDurationForEachView(): Long {
        return durationInMilli() / dotsNumbers
    }

    private fun stopAnimation() {
        primaryAnimation?.cancel()
    }

    fun startAnimation() {
        primaryAnimation?.start()
    }

    override fun setVisibility(visibility: Int) {
        if (visibility == View.VISIBLE) startAnimation()
        else stopAnimation()
        super.setVisibility(visibility)
    }

    companion object {
        fun convertDpToPixel(dp: Float, context: Context): Int {
            return (
                    dp *
                            (
                                    context.resources.displayMetrics.densityDpi.toFloat() /
                                            DisplayMetrics.DENSITY_DEFAULT
                                    )
                    )
                .toInt()
        }
    }
}
