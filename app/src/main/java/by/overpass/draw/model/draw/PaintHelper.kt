package by.overpass.draw.model.draw

import android.graphics.Color
import android.graphics.Paint

/**
 * Created by Alex.S on 10/26/2018.
 */
class PaintHelper private constructor() {

    var color: Int = Color.BLACK
        set(value) {
            field = value
            paint.color = field
        }
    var fill: Boolean = true
        set(value) {
            field = value
            paint.style = if (fill) Paint.Style.FILL else Paint.Style.STROKE
        }
    var paint: Paint = Paint().apply {
        style = if (fill) Paint.Style.FILL else Paint.Style.STROKE
        color = color
    }
        private set

    companion object {
        private var instance: PaintHelper? = null

        fun getInstance(): PaintHelper = instance
                ?: synchronized(this) {
            instance
                    ?: PaintHelper().also {
                instance = it
            }
        }
    }

}