package by.overpass.draw.ui.main.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import by.overpass.draw.CanvasStateHelper
import by.overpass.draw.R
import by.overpass.draw.ui.main.listener.SpinnerItemSelectedListener
import by.overpass.draw.ui.main.listener.tools.BrushTouchListener
import by.overpass.draw.ui.main.listener.tools.CircleTouchListener
import by.overpass.draw.ui.main.listener.tools.LineTouchListener
import by.overpass.draw.ui.main.listener.tools.SquareTouchListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareToolsSpinner()
        canvas.post { CanvasStateHelper.getInstance().addNewState(canvas) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_undo -> {
                CanvasStateHelper.getInstance().undo(canvas)
                true
            }
            R.id.action_redo -> {
                CanvasStateHelper.getInstance().redo(canvas)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun prepareToolsSpinner() {
        spinnerTool.adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.tools)
        )
        spinnerTool.onItemSelectedListener = object : SpinnerItemSelectedListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                canvas.setOnTouchListener(
                        when (position) {
                            0 -> {
                                BrushTouchListener(canvas)
                            }
                            1 -> {
                                LineTouchListener(canvas)
                            }
                            2 -> {
                                CircleTouchListener(canvas)
                            }
                            3 -> {
                                SquareTouchListener(canvas)
                            }
                            4 -> {
                                BrushTouchListener(canvas)
                            }
                            5 -> {
                                BrushTouchListener(canvas)
                            }
                            else -> BrushTouchListener(canvas)
                        }
                )
            }

        }
    }

}
