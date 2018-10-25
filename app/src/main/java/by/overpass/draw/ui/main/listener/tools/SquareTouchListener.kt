package by.overpass.draw.ui.main.listener.tools

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import android.view.View
import by.overpass.draw.CanvasStateHelper
import by.overpass.draw.ui.main.widget.CanvasView
import kotlin.math.min

class SquareTouchListener(canvas: CanvasView) : BaseToolTouchListener(canvas) {


    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            /*MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                attemptToDrawSquare(false)
            }*/
            MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
                attemptToDrawSquare(true)
                clearCoordinates()
            }
        }
        return true
    }

    private fun drawSquare(startX: Float, startY: Float, sideLength: Float) {
        val bitmap = canvas.getBitmap()
        val mutableBitmap = bitmap.copy(bitmap.config, true)
        Canvas(mutableBitmap).apply {
            val paint = Paint().apply {
                style = Paint.Style.FILL
                color = Color.BLACK
            }
            drawRect(startX, startY, startX + sideLength, startY + sideLength, paint)
        }
        canvas.background = BitmapDrawable(canvas.resources, mutableBitmap)
    }

    private fun attemptToDrawSquare(shouldSaveState: Boolean) {
        if (startX != null && startY != null && endX != null && endY != null) {
            val offsetX = Math.abs(endX!! - startX!!)
            val offsetY = Math.abs(endY!! - startY!!)
            val sideLength = min(offsetX, offsetY)
            if (startX!! < endX!! && startY!! < endY!!) {
                drawSquare(startX!!, startY!!, sideLength)
            } else if (startX!! < endX!! && startY!! > endY!!) {
                drawSquare(startX!!, endY!!, sideLength)
            } else if (startX!! > endX!! && startY!! < endY!!) {
                drawSquare(endX!!, startY!!, sideLength)
            } else {
                drawSquare(endX!!, endY!!, sideLength)
            }
            if (shouldSaveState) {
                CanvasStateHelper.getInstance().addNewState(canvas)
            }
        }
    }

}