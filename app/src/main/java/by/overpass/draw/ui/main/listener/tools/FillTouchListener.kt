package by.overpass.draw.ui.main.listener.tools

import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import android.view.View
import by.overpass.draw.model.draw.CanvasStateHelper
import by.overpass.draw.model.draw.PaintHelper
import by.overpass.draw.ui.main.widget.CanvasView
import by.overpass.draw.util.runInBackground
import by.overpass.draw.util.runOnUI
import java.util.*

class FillTouchListener(canvas: CanvasView) : BaseToolTouchListener(canvas) {

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                attemptToFill()
            }
        }
        return true
    }

    private fun attemptToFill() {
        if (startX != null && startY != null) {
            floodFill(startX!!.toInt(), startY!!.toInt())
            CanvasStateHelper.getInstance().addNewState(canvas)
        }
    }

    private fun floodFill(startX: Int, startY: Int) {
        runInBackground {
            val stack = Stack<Point>()
            var x = startX
            var y = startY
            val bitmap = canvas.getBitmap()
            val bitmapWidth = bitmap.width
            val bitmapHeight = bitmap.height

            var point: Point
            var spanAbove: Boolean
            var spanBelow: Boolean
            val oldColor = bitmap.getPixel(x, y)
            val targetColor = PaintHelper.getInstance().color

            if (oldColor == targetColor) {
                return@runInBackground
            }
            stack.clear()
            stack.push(Point(x, y))

            while (stack.size > 0) {
                point = stack.pop()
                x = point.x
                y = point.y
                while (x >= 0 && bitmap.getPixel(x, y) == oldColor) {
                    x--
                }
                x++

                spanBelow = false
                spanAbove = spanBelow
                while (x < bitmapWidth && bitmap.getPixel(x, y) == oldColor) {
                    bitmap.setPixel(x, y, targetColor)

                    if (!spanAbove && y > 0 && bitmap.getPixel(x, y - 1) == oldColor) {
                        stack.push(Point(x, y - 1))
                        spanAbove = true
                    } else if (spanAbove && y > 0 && bitmap.getPixel(x, y - 1) != oldColor) {
                        spanAbove = false
                    }

                    if (!spanBelow && y < bitmapHeight - 1 && bitmap.getPixel(x, y + 1) == oldColor) {
                        stack.push(Point(x, y + 1))
                        spanBelow = true
                    } else if (spanBelow && y < bitmapHeight - 1 && bitmap.getPixel(x, y + 1) != oldColor) {
                        spanBelow = false
                    }

                    x++
                }
            }
            runOnUI {
                canvas.background = BitmapDrawable(canvas.resources, bitmap)
            }
        }
    }

}