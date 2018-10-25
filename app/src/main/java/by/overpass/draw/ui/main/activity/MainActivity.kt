package by.overpass.draw.ui.main.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import by.overpass.draw.R
import by.overpass.draw.ui.main.listener.BrushTouchListener
import by.overpass.draw.ui.main.listener.CircleTouchListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinnerTool.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.tools))
        spinnerTool.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        dvCanvas.setOnTouchListener(BrushTouchListener())
                    }
                    1 -> {
                    }
                    2 -> {
                        dvCanvas.setOnTouchListener(CircleTouchListener(dvCanvas))
                    }
                    3 -> {
                    }
                    4 -> {
                    }
                    5 -> {
                    }
                }
            }

        }
    }

}
