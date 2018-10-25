package by.overpass.draw.ui.main.listener

import android.view.MotionEvent
import android.view.View

/**
 * Created by Alex.S on 10/25/2018.
 */
class BrushTouchListener : View.OnTouchListener {

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        view?.performClick()
        return false
    }

}