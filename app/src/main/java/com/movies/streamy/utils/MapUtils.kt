package com.movies.streamy.utils



import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.movies.streamy.R
import com.movies.streamy.utils.Constants.Companion.MAP_CAMERA_ZOOM_INT

import kotlin.math.atan
import kotlin.math.roundToInt


object MapUtil {

    fun GoogleMap.moveCameraOnMapBound(latLng: LatLngBounds?) {
        latLng?.let { CameraUpdateFactory.newLatLngBounds(it, MAP_CAMERA_ZOOM_INT) }
            ?.let { this.animateCamera(it) }
    }

    fun createMarker(
        markerTarget: LatLng?, isFlat: Boolean,
        iconBitmapDescriptor: BitmapDescriptor?
    ): MarkerOptions? {
        return markerTarget?.let {
            MarkerOptions().position(it)
                .flat(isFlat)
                .icon(iconBitmapDescriptor)
        }
    }

    fun createCameraPosition(target: MutableList<LatLng>, cameraObliqueZoom: Float): CameraPosition {
        return CameraPosition.Builder()
            .target(target[0])
            .zoom(cameraObliqueZoom)
            .bearing(SphericalUtil.computeHeading(target.firstOrNull(), target.lastOrNull()).toFloat())
            .build()
    }

    fun getMarkerBitmapFromView(@DrawableRes resId: Int, customMarkerView: View): Bitmap? {

        val markerImageView = customMarkerView.findViewById<View>(R.id.profile_image) as ImageView
        markerImageView.setImageResource(resId)
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(
            0,
            0,
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight
        )
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth, customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        drawable?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

    /**
     *  Rotate marker
     **/
    fun computeRotation(fraction: Float, start: Float, end: Float): Float {
        val normalizeEnd = end - start // rotate start to 0
        val normalizedEndAbs = (normalizeEnd + 360) % 360

        val direction =
            (if (normalizedEndAbs > 180) -1 else 1).toFloat() // -1 = anticlockwise, 1 = clockwise
        val rotation: Float = if (direction > 0) {
            normalizedEndAbs
        } else {
            normalizedEndAbs - 360
        }

        val result = fraction * rotation + start
        return (result + 360) % 360
    }

    /**
     *  Find GPS rotate position
     **/
    fun bearing(startPoint: Location, endPoint: Location): Double {
        val deltaLongitude = endPoint.longitude - startPoint.longitude
        val deltaLatitude = endPoint.latitude - startPoint.latitude
        val angle = 3.14 * .5f - atan(deltaLatitude / deltaLongitude)

        if (deltaLongitude > 0)
            return angle
        else if (deltaLongitude < 0)
            return angle + 3.14
        else if (deltaLatitude < 0) return 3.14

        return 0.0
    }

    fun showLocation(googleMap: GoogleMap?, location: LatLng?, zoom: Float = 14f) {
        if (googleMap != null && location != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
        }
    }
    fun valueAnimate(accuracy: Float, updateListener: ValueAnimator.AnimatorUpdateListener): ValueAnimator {
        val va = ValueAnimator.ofFloat(0F, accuracy)
        va.duration = 8000
        va.addUpdateListener(updateListener)
        va.repeatCount = ValueAnimator.INFINITE
        va.repeatMode = ValueAnimator.REVERSE

        va.start()
        return va
    }

    fun getDisplayPulseRadius(googleMap: GoogleMap?, radius: Float): Float {
        if (googleMap != null) {
            val diff = googleMap.maxZoomLevel - googleMap.cameraPosition.zoom
            if (diff < 3)
                return radius
            if (diff < 3.7)
                return radius * (diff / 2)
            if (diff < 4.5)
                return radius * diff
            if (diff < 5.5)
                return radius * diff * 1.5f
            if (diff < 7)
                return radius * diff * 2f
            if (diff < 7.8)
                return radius * diff * 3.5f
            if (diff < 8.5)
                return radius * diff * 5f
            if (diff < 10)
                return radius * diff * 10f
            if (diff < 12)
                return radius * diff * 18f
            if (diff < 13)
                return radius * diff * 28f
            if (diff < 16)
                return radius * diff * 40f
            return if (diff < 18) radius * diff * 60 else radius * diff * 80
        } else {
            return 0f
        }
    }

    fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}