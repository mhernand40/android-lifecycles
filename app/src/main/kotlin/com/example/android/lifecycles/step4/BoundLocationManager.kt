/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.lifecycles.step4

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log


object BoundLocationManager {
    fun bindLocationListenerIn(
            lifecycleOwner: LifecycleOwner,
            listener: LocationListener, context: Context
    ) {
        BoundLocationListener(lifecycleOwner, listener, context)
    }

    internal class BoundLocationListener(
            lifecycleOwner: LifecycleOwner,
            private val listener: LocationListener,
            private val context: Context
    ) : LifecycleObserver {
        //TODO: Add lifecycle observer
        private var locationManager: LocationManager? = null

        //TODO: Call this on resume
        fun addLocationListener() {
            // Note: Use the Fused Location Provider from Google Play Services instead.
            // https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderApi

            locationManager = context.locationManager.apply {
                requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, listener)
                Log.d("BoundLocationMgr", "Listener added")

                // Force an update with the last location, if available.
                getLastKnownLocation(LocationManager.GPS_PROVIDER)?.apply {
                    listener.onLocationChanged(this)
                }
            }
        }

        //TODO: Call this on pause
        fun removeLocationListener() {
            locationManager?.apply {
                removeUpdates(listener)
                Log.d("BoundLocationMgr", "Listener removed")
            }
            locationManager = null
        }
    }
}

private val Context.locationManager: LocationManager
    get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager
