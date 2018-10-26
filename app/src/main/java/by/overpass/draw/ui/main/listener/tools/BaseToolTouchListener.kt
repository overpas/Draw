package by.overpass.draw.ui.main.listener.tools

import android.view.View
import androidx.annotation.CallSuper
import by.overpass.draw.ui.main.widget.CanvasView

abstract class BaseToolTouchListener(protected val canvas: CanvasView) : View.OnTouchListener {

    protected var startX: Float? = null
    protected var startY: Float? = null
    protected var endX: Float? = null
    protected var endY: Float? = null

    @CallSuper
    protected open fun clearCoordinates() {
        startX = null
        startY = null
        endX = null
        endY = null
    }

}