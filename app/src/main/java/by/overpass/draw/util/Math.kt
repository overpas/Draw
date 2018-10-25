package by.overpass.draw.util

fun calculateDistance(startX: Float, startY: Float, endX: Float, endY: Float) =
        Math.sqrt(Math.pow((endX - startX).toDouble(), 2.0)
                + Math.pow((endY - startY).toDouble(), 2.0)).toFloat()