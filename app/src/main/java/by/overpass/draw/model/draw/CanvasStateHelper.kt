package by.overpass.draw.model.draw

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import by.overpass.draw.ui.main.widget.CanvasView

class CanvasStateHelper private constructor() {

    private val bitmaps: MutableList<Bitmap> = mutableListOf()
    var current: Int = -1
        private set

    fun addNewState(canvas: CanvasView) {
        if (current == -1) {
            bitmaps.add(canvas.getBitmap())
            current++
        } else {
            bitmaps.add(++current, canvas.getBitmap())
            val size = bitmaps.size
            for (i in current + 1 until size) {
                bitmaps.removeAt(bitmaps.lastIndex)
            }
        }
    }

    fun undo(canvas: CanvasView) {
        if (current > 0) {
            canvas.background = BitmapDrawable(canvas.resources, bitmaps[--current])
        }
    }

    fun redo(canvas: CanvasView) {
        if (bitmaps.size - 1 > current) {
            canvas.background = BitmapDrawable(canvas.resources, bitmaps[++current])
        }
    }

    companion object {
        private var instance: CanvasStateHelper? = null

        fun getInstance(): CanvasStateHelper = instance
                ?: synchronized(this) {
            instance
                    ?: CanvasStateHelper().also {
                instance = it
            }
        }
    }

}