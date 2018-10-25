package by.overpass.draw

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import by.overpass.draw.ui.main.widget.CanvasView

class CanvasStateHelper private constructor() {

    private val bitmaps: MutableList<Bitmap> = mutableListOf()
    var current: Int = -1
        private set

    fun addNewState(canvas: CanvasView) {
        bitmaps.add(canvas.getBitmap())
        current++
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

        fun getInstance(): CanvasStateHelper = instance ?: synchronized(this) {
            instance ?: CanvasStateHelper().also {
                instance = it
            }
        }
    }

}