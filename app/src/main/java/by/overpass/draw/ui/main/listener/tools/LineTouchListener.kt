package by.overpass.draw.ui.main.listener.tools

import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import android.view.View
import by.overpass.draw.model.draw.CanvasStateHelper
import by.overpass.draw.model.draw.PaintHelper
import by.overpass.draw.ui.main.widget.CanvasView

class LineTouchListener(canvas: CanvasView) : BaseToolTouchListener(canvas) {

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
                attemptToDrawLine(true)
                clearCoordinates()
            }
        }
        return true
    }

    private fun drawLine(startX: Float, startY: Float, endX: Float, endY: Float) {
        val bitmap = canvas.getBitmap()
        val mutableBitmap = bitmap.copy(bitmap.config, true)
        Canvas(mutableBitmap).apply {
            val paint = PaintHelper.getInstance().paint
            drawLine(startX, startY, endX, endY, paint)
        }
        canvas.background = BitmapDrawable(canvas.resources, mutableBitmap)
    }

    private fun attemptToDrawLine(shouldSaveState: Boolean) {
        if (startX != null && startY != null && endX != null && endY != null) {
            drawLine(startX!!, startY!!, endX!!, endY!!)
            if (shouldSaveState) {
                CanvasStateHelper.getInstance().addNewState(canvas)
            }
        }
    }

}