package com.roacult.expandabletoolbar

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class ExpandableToolbar @JvmOverloads constructor(context: Context, attribute : AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context,attribute,defStyleAttr) {

    var failColor = DEFAULT_FAIL_COLOR
    var successColor = DEFAULT_SUCCESS_COLOR
    var failText = DEFAULT_FAIL_TEXT
    var successText= DEFAULT_SUCCESS_TEXT
    var animationDuration = DEFAULT_ANIMATION_DURATION
    var hidingDuration = DEFAULT_HIDING_DURATION

    init {
        val typedArray = context.obtainStyledAttributes(attribute , R.styleable.ExpandableToolbar)

        failColor = typedArray.getColor(R.styleable.ExpandableToolbar_failColor, DEFAULT_FAIL_COLOR)
        successColor = typedArray.getColor(R.styleable.ExpandableToolbar_failColor, DEFAULT_SUCCESS_COLOR)
        failText = typedArray.getString(R.styleable.ExpandableToolbar_failText) ?: DEFAULT_FAIL_TEXT
        successText = typedArray.getString(R.styleable.ExpandableToolbar_successText) ?: DEFAULT_SUCCESS_TEXT
        animationDuration = typedArray.getInt(R.styleable.ExpandableToolbar_animationDuration, DEFAULT_ANIMATION_DURATION.toInt()).toLong()
        hidingDuration = typedArray.getInt(R.styleable.ExpandableToolbar_hidingDuration,-1)


        typedArray.recycle()
    }

    companion object {
        const val DEFAULT_FAIL_COLOR = -5242848
        const val DEFAULT_SUCCESS_COLOR = -15701248
        const val DEFAULT_TEXT_COLOR = -1
        const val DEFAULT_FAIL_TEXT = "there is no internet connection"
        const val DEFAULT_SUCCESS_TEXT = "you are connected"

        const val DEFAULT_ANIMATION_DURATION = 300L
        const val DEFAULT_HIDING_DURATION = 3000
    }
}