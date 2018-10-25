package by.overpass.draw.ui.main.listener.tools

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import android.view.View
import by.overpass.draw.CanvasStateHelper
import by.overpass.draw.ui.main.widget.CanvasView

class BrushTouchListener(canvas: CanvasView) : BaseToolTouchListener(canvas) {

    private var currentX: Float? = null
    private var currentY: Float? = null

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            attemptToDrawPoint(true)
            clearCoordinates()
            return true
        }
        attemptToInterpolate(event.x, event.y)
        currentX = event.x
        currentY = event.y
        attemptToDrawPoint(false)
        return true
    }

    override fun clearCoordinates() {
        super.clearCoordinates()
        currentX = null
        currentY = null
    }

    private fun drawPoint(currentX: Float, currentY: Float, radius: Float) {
        val bitmap = canvas.getBitmap()
        val mutableBitmap = bitmap.copy(bitmap.config, true)
        Canvas(mutableBitmap).apply {
            val paint = Paint().apply {
                style = Paint.Style.FILL
                color = Color.BLACK
            }
            drawCircle(currentX, currentY, radius, paint)
        }
        canvas.background = BitmapDrawable(canvas.resources, mutableBitmap)
    }

    private fun attemptToDrawPoint(shouldSaveState: Boolean) {
        if (currentX != null && currentY != null) {
            drawPoint(currentX!!, currentY!!, 2F)
            if (shouldSaveState) {
                CanvasStateHelper.getInstance().addNewState(canvas)
            }
        }
    }

    private fun attemptToInterpolate(nextX: Float, nextY: Float) {
        if (currentX != null && currentY != null) {
            drawLine(currentX!!, currentY!!, nextX, nextY)
        }
    }

    private fun drawLine(currentX: Float, currentY: Float, nextX: Float, nextY: Float) {
        val bitmap = canvas.getBitmap()
        val mutableBitmap = bitmap.copy(bitmap.config, true)
        Canvas(mutableBitmap).apply {
            val paint = Paint().apply {
                style = Paint.Style.FILL
                color = Color.BLACK
                strokeWidth = 4F
            }
            drawLine(currentX, currentY, nextX, nextY, paint)
        }
        canvas.background = BitmapDrawable(canvas.resources, mutableBitmap)
    }

}