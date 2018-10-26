package by.overpass.draw.model.file

import java.io.File

interface OnImageSavedCallback {
    fun onSuccess()

    fun onFail()

    fun notifyGallery(file: File)
}