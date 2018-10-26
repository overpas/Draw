package by.overpass.draw.model.file

import android.graphics.Bitmap
import android.media.ImageWriter
import android.os.Environment
import android.util.Log

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

import by.overpass.draw.util.runInBackground
import by.overpass.draw.util.runOnUI

/**
 * Created by MAX on 21.03.2017.
 */

class ImageSaver {

    fun writeFile(bitmap: Bitmap,
                  fileName: String,
                  listener: OnImageSavedCallback) {
        // path to /data/data/yourapp/app_data/imageDir
        runInBackground {
            val directory = imageFolder
            if (directory.mkdirs() || directory.exists() && directory.isDirectory) {
                // Create imageDir
                val file = File(directory, "$fileName.jpg")
                try {
                    FileOutputStream(file).use { fos ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        runOnUI { listener.onSuccess() }
                        listener.notifyGallery(file)
                    }
                } catch (e: IOException) {
                    Log.e(TAG, e.message, e)
                    runOnUI { listener.onFail() }
                }

            } else {
                runOnUI { listener.onFail() }
            }
        }
    }

    companion object {

        private val TAG = ImageSaver::class.java.simpleName
        private val IMAGE_FOLDER_NAME = "imageDir"

        @JvmStatic
        val imageFolder: File
            get() {
                val commonPicturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                return File(commonPicturesDirectory, IMAGE_FOLDER_NAME)
            }
    }

}