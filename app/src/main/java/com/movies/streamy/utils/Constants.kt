package com.movies.streamy.utils

import com.movies.streamy.BuildConfig

class Constants {
    companion object {
        //Units
        const val METRIC = "metric"

        //Main Weather
        const val CLOUDY = "Clouds"
        const val SUNNY = "Clear"
        const val RAINY = "Rain"

        //location
        const val REQUEST_CHECK_SETTINGS = 0x1
        const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 30000
        const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2

        //Shared Prefs
        const val APP_SHARED_PREFRENCES_NAME = BuildConfig.APPLICATION_ID + "Prefs"

        // Map
        const val POLYLINE_WIDTH = 11f
        const val CAMERA_OBLIQUE_ZOOM = 16f
        const val MAP_CAMERA_ZOOM_INT = 11

        //OnBoarding
        const val PHONE_METHOD = "phone"

        //Location broadcast
        private const val PACKAGE_NAME = "com.riley.driver"
        const val ACTION_BROADCAST_LOCATION = "$PACKAGE_NAME.broadcast"
        const val EXTRA_LOCATION = "$PACKAGE_NAME.location"

        // ROOM Database
        const val DATABASE_NAME = "riley_driver_database"

        //Keys
        const val KEY_BUILDING_ID = "building_id"
        const val KEY_IS_UNIT_REQUEST = "is_unit"
        const val KEY_TENANT_ID = "tenant_id"

        const val KEY_TICKET_ID = "ticket_id"
        const val KEY_TICKET_MESSAGE = "ticket_message"
        const val KEY_TICKET_TIME = "ticket_time"
    }
}