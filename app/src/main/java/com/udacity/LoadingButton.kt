package com.udacity

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates
private const val TEXT_SIZE = 55f
private const val TEXT_OFFSET = TEXT_SIZE / 2
private const val ANIMATION_STEPS = 100
private const val ANIMATION_DURATION = 1_500L


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private val paint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = TEXT_SIZE
    }

    private val rect = Rect()

    private val loadingRect = Rect()

    private var buttonText = ""

    private val ovalContainer = RectF()

    private val textBounds = Rect()

    private var angle = 0f

    private var textLoading = ""

    private var textNormal = ""

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            textNormal = getString(R.styleable.LoadingButton_textNormal).toString()
            textLoading = getString(R.styleable.LoadingButton_textLoading).toString()
        }
        buttonText = textNormal
    }

    override fun performClick(): Boolean {
        valueAnimator.setIntValues(0, ANIMATION_STEPS)
        loadingRect.bottom = heightSize
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            loadingRect.right = value * widthSize / ANIMATION_STEPS
            buttonText = textLoading
            angle = (value * 360 / ANIMATION_STEPS).toFloat()
            invalidate()
        }
        valueAnimator.addListener(
            object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    isClickable = false
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isClickable = true
                    buttonText = textNormal
                    loadingRect.right = 0
                    angle = 0f
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationRepeat(animation: Animator?) {}

            }
        )
        valueAnimator.duration = ANIMATION_DURATION
        valueAnimator.start()

        return super.performClick()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = context.getColor(R.color.colorPrimary)
        rect.run {
            right = widthSize
            bottom = heightSize
        }
        canvas?.drawRect(rect, paint)

        paint.color = context.getColor(R.color.colorPrimaryDark)
        canvas?.drawRect(loadingRect, paint)

        paint.color = Color.WHITE
        canvas?.drawText(
            buttonText,
            rect.exactCenterX(),
            rect.exactCenterY() + TEXT_OFFSET,
            paint
        )

        paint.run {
            color = context.getColor(R.color.colorAccent)
            getTextBounds(buttonText, 0, buttonText.length, textBounds)
        }
        ovalContainer.run {
            top = rect.exactCenterY() - (TEXT_OFFSET)
            left = rect.exactCenterX() + (textBounds.right / 2) + TEXT_OFFSET
            right = left + TEXT_SIZE
            bottom = top + TEXT_SIZE
        }
        canvas?.drawArc(ovalContainer, 0f, angle, true, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}
