package com.movies.streamy

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.BuildConfig
import com.movies.streamy.model.repository.FloatingViewRepository.updateForegroundStatus
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class StreamyApp : Application() , LifecycleObserver {

    @Override
    override fun onCreate()  {
        super.onCreate()
        setUpTimber()
    }

    private fun setUpTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format(
                        "Class:%s: Line: %s, Method: %s",
                        super.createStackElementTag(element),
                        element.lineNumber,
                        element.methodName
                    )
                }
            })
        }
    }


    //Check Foreground status
//    override fun onStart(owner: LifecycleOwner) {
//        super.onStart(owner)
//        updateForegroundStatus(true)
//    }
//
//    override fun onStop(owner: LifecycleOwner) {
//        super.onStart(owner)
//        updateForegroundStatus(false)
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        updateForegroundStatus(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        updateForegroundStatus(false)
    }
}