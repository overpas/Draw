package by.overpass.draw.ui.main.listener

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import android.view.View
import com.divyanshu.draw.widget.DrawView

/**
 * Created by Alex.S on 10/25/2018.
 */
class CircleTouchListener(private val canvas: DrawView) : View.OnTouchListener {

    private var initX: Float? = null
    private var initY: Float? = null
    private var endX: Float? = null
    private var endY: Float? = null

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initX = event.x
                initY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                attemptToDrawCircle()
            }
            MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
                attemptToDrawCircle()
                clearCoordinates()
            }
        }
        return true
    }

    private fun drawCircle(centerX: Float, centerY: Float, radius: Float) {
        val bitmap = canvas.getBitmap()
        val mutableBitmap = bitmap.copy(bitmap.config, true)
        Canvas(mutableBitmap).apply {
            val paint = Paint().apply {
                style = Paint.Style.FILL
                color = Color.BLACK
            }
            drawCircle(centerX, centerY, radius, paint)
        }
        canvas.background = BitmapDrawable(canvas.resources, mutableBitmap)
    }

    private fun attemptToDrawCircle() {
        if (initX != null && initY != null && endX != null && endY != null) {
            val radius = Math.sqrt(Math.pow((endX!! - initX!!).toDouble(), 2.0) + Math.pow((endY!! - initY!!).toDouble(), 2.0)).toFloat()
            drawCircle(initX!!, initY!!, radius)
        }
    }

    private fun clearCoordinates() {
        initX = null
        initY = null
        endX = null
        endY = null
    }

}