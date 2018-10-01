/*
 * Copyright 2017 Comcast Cable Communications Management, LLC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xfinity.dahdit;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class DottedRing
@JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private var dotRadius: Float
    private var minimumDotGap: Float

    private val paint: Paint = Paint()
    private val ringMatrix = Matrix()

    init {
        val metrics = resources.displayMetrics
        val twoDpDefault = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, metrics)
        val defaultBlack = Color.argb(255, 0, 0, 0)

        val typedArray = context?.theme?.obtainStyledAttributes(attrs, R.styleable.DottedItem, defStyleAttr, 0)

        dotRadius = typedArray?.getDimension(R.styleable.DottedItem_dotRadius, twoDpDefault) ?: twoDpDefault
        minimumDotGap = typedArray?.getDimension(R.styleable.DottedItem_minimumDotGap, twoDpDefault) ?: dotRadius
        paint.color = typedArray?.getColor(R.styleable.DottedItem_dotColor, defaultBlack) ?: defaultBlack

        typedArray?.recycle()

        paint.style = Paint.Style.FILL
        paint.flags = Paint.ANTI_ALIAS_FLAG
    }

    override fun onDraw(canvas: Canvas) {
        val radius = computeRadius()
        val cx = paddingLeft + radius + dotRadius
        val cy = paddingTop + radius + dotRadius

        val count = ((2 * Math.PI * radius) / (dotRadius * 2 + minimumDotGap)).toInt()

        val angleDegrees = (360F / count)

        for (i in 0 until count) {
            val angle = angleDegrees * i

            ringMatrix.setRotate(angle, cx, cy)

            canvas.matrix = ringMatrix
            canvas.drawCircle(cx, cy - radius, dotRadius, paint)
        }
    }

    private fun computeRadius(): Float {
        val cx = ((width - paddingLeft - paddingRight) / 2).toFloat()
        val cy = ((height - paddingTop - paddingBottom) / 2).toFloat()

        return Math.min(cx, cy) - dotRadius
    }
}