package by.overpass.draw.ui.main.listener.tools

import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import android.view.View
import by.overpass.draw.model.draw.CanvasStateHelper
import by.overpass.draw.model.draw.PaintHelper
import by.overpass.draw.ui.main.widget.CanvasView

/**
 * Created by Alex.S on 10/26/2018.
 */
class RectangleTouchListener(canvas: CanvasView) : BaseToolTouchListener(canvas) {

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            /*MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                attemptToDrawRectangle(false)
            }*/
            MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
                attemptToDrawRectangle(true)
                clearCoordinates()
            }
        }
        return true
    }

    private fun drawRectangle(startX: Float, startY: Float, endX: Float, endY: Float) {
        val bitmap = canvas.getBitmap()
        val mutableBitmap = bitmap.copy(bitmap.config, true)
        Canvas(mutableBitmap).apply {
            val paint = PaintHelper.getInstance().paint
            drawRect(startX, startY, endX, endY, paint)
        }
        canvas.background = BitmapDrawable(canvas.resources, mutableBitmap)
    }

    private fun attemptToDrawRectangle(shouldSaveState: Boolean) {
        if (startX != null && startY != null && endX != null && endY != null) {
            drawRectangle(startX!!, startY!!, endX!!, endY!!)
            if (shouldSaveState) {
                CanvasStateHelper.getInstance().addNewState(canvas)
            }
        }
    }

}