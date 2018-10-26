package by.overpass.draw.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

private val uiHandler = Handler(Looper.getMainLooper())
private val backgroundExecutor = Executors.newFixedThreadPool(2)

fun runOnUI(task: () -> Unit) {
    uiHandler.post(task)
}

fun runInBackground(task: () -> Unit) {
    backgroundExecutor.execute(task)
}
