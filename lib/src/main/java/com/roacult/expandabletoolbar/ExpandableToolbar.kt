package com.roacult.expandabletoolbar

import android.animation.ValueAnimator
import android.content.Context
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ExpandableToolbar @JvmOverloads constructor(context: Context, attribute : AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context,attribute,defStyleAttr) {

    private val view = LayoutInflater.from(context).inflate(R.layout.expandable_layout,this,true)
    private val textView = view.findViewById<TextView>(R.id.connetivity_state_text)
    val toolbar = view.findViewById<Toolbar>(R.id.expand_toolbar)!!

    private val toolbarHeight = Math.round(dpToPx(56))
    private var expansion = 0F
    private val interpolator = FastOutSlowInInterpolator()
    private var disposable : Disposable? = null
    private var firstTime = true
    private val animator = ValueAnimator.ofFloat()
    private val hideTextView = Runnable { animateTextView(0f) }

    var failColor = DEFAULT_FAIL_COLOR
    var successColor = DEFAULT_SUCCESS_COLOR
    var failText = DEFAULT_FAIL_TEXT
    var successText= DEFAULT_SUCCESS_TEXT
    var animationDuration = DEFAULT_ANIMATION_DURATION
    var hidingDuration = DEFAULT_HIDING_DURATION
    var isConnected = false
        private set

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return Bundle().apply {
            putParcelable(SUPER_STATE_KEY,superState)
            putFloat(EXPANSION_KEY,expansion)
            putBoolean(CONNECTION_STATE_KEY,isConnected)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        super.onRestoreInstanceState(bundle.getParcelable(SUPER_STATE_KEY))
        expansion = bundle.getFloat(EXPANSION_KEY)
        isConnected = bundle.getBoolean(CONNECTION_STATE_KEY)
        post{
            upDateState(false)
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(attribute , R.styleable.ExpandableToolbar)

        failColor = typedArray.getColor(R.styleable.ExpandableToolbar_failColor, DEFAULT_FAIL_COLOR)
        successColor = typedArray.getColor(R.styleable.ExpandableToolbar_failColor, DEFAULT_SUCCESS_COLOR)
        failText = typedArray.getString(R.styleable.ExpandableToolbar_failText) ?: DEFAULT_FAIL_TEXT
        successText = typedArray.getString(R.styleable.ExpandableToolbar_successText) ?: DEFAULT_SUCCESS_TEXT
        animationDuration = typedArray.getInt(R.styleable.ExpandableToolbar_animationDuration, DEFAULT_ANIMATION_DURATION.toInt()).toLong()
        hidingDuration = typedArray.getInt(R.styleable.ExpandableToolbar_hidingDuration,-1)

        val textColor = typedArray.getColor(R.styleable.ExpandableToolbar_textColor, DEFAULT_TEXT_COLOR)
        textView.setTextColor(textColor)

        typedArray.recycle()
    }

    private fun animateTextView( to: Float ){
        animator.cancel()
        animator.apply {
            setFloatValues( expansion , to )
            duration = animationDuration
            interpolator = this@ExpandableToolbar.interpolator
            addUpdateListener {
                setExpansion(it.animatedValue as Float)
            }
            start()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handleInternetState()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable?.dispose()
        handler.removeCallbacks(hideTextView)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        textView.visibility = if( expansion == 0F ) GONE else VISIBLE
        textView.translationY = textView.height*expansion

        val expansionDelta = Math.round(textView.measuredHeight*expansion)
        setMeasuredDimension(measuredWidth,toolbarHeight + expansionDelta)
    }

    private fun handleInternetState() {
        disposable?.dispose()
        disposable = ReactiveNetwork.observeNetworkConnectivity(context).
            subscribeOn(Schedulers.io()).
            observeOn(AndroidSchedulers.mainThread()).
            subscribe {
                if (firstTime) {
                    isConnected = it.state() != NetworkInfo.State.CONNECTED
                    firstTime = false
                }
                if (it.state() == NetworkInfo.State.CONNECTED && !isConnected) {
                    isConnected = true
                    upDateState(true)
                } else if (it.state() != NetworkInfo.State.CONNECTED && isConnected) {
                    isConnected = false
                    upDateState(true)
                }
            }
    }

    private fun upDateState(withAnimation : Boolean) {
        updateTextViewProperties()

        if( isConnected && expansion>0 ){
            //hide expanded view
            handler.postDelayed(hideTextView, hidingDuration.toLong() )
        }else if( expansion < 1  && !isConnected ) {
            //show expanded view
            if (withAnimation) animateTextView(1F)
            else setExpansion(1F)
        }

    }

    private fun updateTextViewProperties() {
        if(isConnected) {
            textView.text = successText
            textView.setBackgroundColor(successColor)
        }else {
            textView.text = failText
            textView.setBackgroundColor(failColor)
        }
    }

    private fun setExpansion(exp : Float) {
        expansion = exp
        requestLayout()
    }

    private fun dpToPx(dp : Int ) : Float {
        return dp * context.resources.displayMetrics.density
    }

    companion object {

        const val SUPER_STATE_KEY = "super_state_key"
        const val EXPANSION_KEY = "expansion_key"
        const val CONNECTION_STATE_KEY = "connection_state_key"

        const val DEFAULT_FAIL_COLOR = -5242848
        const val DEFAULT_SUCCESS_COLOR = -15701248
        const val DEFAULT_TEXT_COLOR = -1
        const val DEFAULT_FAIL_TEXT = "no internet connection"
        const val DEFAULT_SUCCESS_TEXT = "you are connected"

        const val DEFAULT_ANIMATION_DURATION = 300L
        const val DEFAULT_HIDING_DURATION = 3000
    }
}