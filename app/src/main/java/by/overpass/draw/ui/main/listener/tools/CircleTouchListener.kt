package by.overpass.draw.ui.main.listener.tools

import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import android.view.View
import by.overpass.draw.model.draw.CanvasStateHelper
import by.overpass.draw.model.draw.PaintHelper
import by.overpass.draw.ui.main.widget.CanvasView
import by.overpass.draw.util.calculateDistance

class CircleTouchListener(canvas: CanvasView) : BaseToolTouchListener(canvas) {

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            /*MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                attemptToDrawCircle(false)
            }*/
            MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
                attemptToDrawCircle(true)
                clearCoordinates()
            }
        }
        return true
    }

    private fun drawCircle(centerX: Float, centerY: Float, radius: Float) {
        val bitmap = canvas.getBitmap()
        val mutableBitmap = bitmap.copy(bitmap.config, true)
        Canvas(mutableBitmap).apply {
            val paint = PaintHelper.getInstance().paint
            drawCircle(centerX, centerY, radius, paint)
        }
        canvas.background = BitmapDrawable(canvas.resources, mutableBitmap)
    }

    private fun attemptToDrawCircle(shouldSaveState: Boolean) {
        if (startX != null && startY != null && endX != null && endY != null) {
            val radius = calculateDistance(startX!!, startY!!, endX!!, endY!!)
            drawCircle(startX!!, startY!!, radius)
            if (shouldSaveState) {
                CanvasStateHelper.getInstance().addNewState(canvas)
            }
        }
    }

}