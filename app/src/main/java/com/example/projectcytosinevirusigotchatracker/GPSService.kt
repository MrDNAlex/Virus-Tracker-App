package com.example.projectcytosinevirusigotchatracker

import android.Manifest
import android.app.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.provider.Settings.Secure
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*


object DistanceTracker {
    var totalDistance: Long = 0L
}

class GPSService : Service() {

    private val localBinder = LocalBinder()

    var JArrayLat = JSONArray()
    var JArrayLon = JSONArray()
    var JArrayPos = JSONArray()

    var JObject = JSONObject()

    var Accuracy = LocationRequest.PRIORITY_HIGH_ACCURACY

    var Interval: Long = 5000
    val FastInterval: Long = 5000

    lateinit var fuseLoc: FusedLocationProviderClient
    lateinit var LocReq: LocationRequest
    lateinit var LocCallBack: LocationCallback

    lateinit var location: Location
    lateinit var notificationManager: NotificationManager

    private val NOTIFICATION_CHANNEL_ID = "Chan ID"


    //Video
    //https://www.youtube.com/watch?v=_xUcYfbtfsI


    override fun onCreate() {
        super.onCreate()

        //Maybe don't use device Id's
        val deviceID = Secure.ANDROID_ID


        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        LocReq = LocationRequest()
        LocReq.setInterval(Interval) //Millisecs?
        LocReq.setFastestInterval(FastInterval)
        LocReq.setPriority(Accuracy)

        fuseLoc = LocationServices.getFusedLocationProviderClient(this)

        LocCallBack = object : LocationCallback() {
            public override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                var Coords = JSONArray()
                val currentTime = Calendar.getInstance().time
                var dateYear = SimpleDateFormat("yy").format(currentTime)
                var dateMonth = SimpleDateFormat("MM").format(currentTime)
                var dateDay = SimpleDateFormat("dd").format(currentTime)
                var dateHour = SimpleDateFormat("HH").format(currentTime)
                var dateMin = SimpleDateFormat("mm").format(currentTime)
                var dateSec = SimpleDateFormat("ss").format(currentTime)


                location = locationResult.lastLocation

                Coords.put(dateYear)
                Coords.put(dateMonth)
                Coords.put(dateDay)
                Coords.put(dateHour)
                Coords.put(dateMin)
                Coords.put(dateSec)
                Coords.put(location.latitude)
                Coords.put(location.longitude)
                Coords.put(location.altitude)
                Coords.put(location.accuracy)

                JArrayPos.put(Coords)

                // JArrayLat.put(location.getLatitude())
                // JArrayLon.put(location.getLongitude())

                JObject.put("Locations", JArrayPos)


                saveToStorage("Coords", JObject)


                //
                //Not Uploaded
                //
                try {
                    var UploadFile = JSONObject(loadJsonFile("UploadLocation" + ".json"))
                    var UploadLocation = UploadFile.getJSONArray("Locations")

                    UploadLocation.put(Coords)

                    saveToStorage("UploadLocation", UploadFile)

                } catch (e: JSONException) {
                    e.printStackTrace();
                }

                //
                //Week File
                //
                try {
                    var WeekFile = JSONObject(loadJsonFile("WeekLocation" + ".json"))
                    var WeekLocation = WeekFile.getJSONArray("Locations")

                    WeekLocation.put(Coords)

                    saveToStorage("WeekLocation", WeekFile)

                } catch (e: JSONException) {
                    e.printStackTrace();
                }

                //
                //Master File
                //
                try {
                    var MasterFile = JSONObject(loadJsonFile("MasterLocation" + ".json"))
                    var MasterLocation = MasterFile.getJSONArray("Locations")

                    MasterLocation.put(Coords)

                    saveToStorage("MasterLocation", MasterFile)

                } catch (e: JSONException) {
                    e.printStackTrace();
                }

                //
                //Emergency Location
                //
                try {
                    var EmerFile = JSONObject(loadJsonFile("EmergencyLocation" + ".json"))
                    // var deviceID = EmerFile.getJSONArray("DeviceID")
                    //var accountID = EmerFile.getJSONArray("AccountID")
                    var emerLocation = JSONArray()

                    var weekFile = JSONObject(loadJsonFile("WeekLocation" + ".json"))
                    var weekLocations = weekFile.getJSONArray("Locations")

                    var weekLength = weekLocations.length()

                    if ((weekLength - 7) >= 0) {
                        for (i in weekLength - 7..weekLength) {
                            emerLocation.put(weekLocations.get(i))
                        }
                    }
                    EmerFile.put("Locations", emerLocation)

                    saveToStorage("EmergencyLocation", EmerFile)

                } catch (e: JSONException) {
                    e.printStackTrace();
                }


                /*
                val geocoder = Geocoder(applicationContext, Locale.getDefault())

                val addresses: List<Address> = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                val address: String = addresses[0].getAddressLine(0)
                val city: String = addresses[0].getLocality()
                val state: String = addresses[0].getAdminArea()
                val zip: String = addresses[0].getPostalCode()
                val country: String = addresses[0].getCountryName()


                 */
                val hi = 0;

/*
                for (location in locationResult.locations) {
                    // Update UI with location data
                    // ...
                }

 */
            }
        }

        startLocationUpdates()

    }

    fun getSettings() {
        try {
            var SettingsFile = JSONObject(loadJsonFile("Settings" + ".json"))

            var callSpeed = SettingsFile.getJSONArray("Frequency")
            var callAccuracy = SettingsFile.getJSONArray("Accuracy")

            Interval = callSpeed.get(0) as Long
            Accuracy = callAccuracy.get(0) as Int

        } catch (e: JSONException) {
            e.printStackTrace();
        }

    }

    fun subscribeToLocationUpdates() {
        Log.d(TAG, "subscribeToLocationUpdates()")

        //  SharedPreferenceUtil.saveLocationTrackingPref(this, true)

        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        startService(Intent(applicationContext, GPSService::class.java))

        try {
            // TODO: Step 1.5, Subscribe to location changes.

        } catch (unlikely: SecurityException) {
            // SharedPreferenceUtil.saveLocationTrackingPref(this, false)
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    inner class LocalBinder : Binder() {
        internal val service: GPSService
            get() = this@GPSService
    }


    fun stopLocation() {
        fuseLoc.removeLocationUpdates(LocCallBack)

    }

    fun startLocationUpdates() {
        checkPermission()
        fuseLoc.requestLocationUpdates(LocReq, LocCallBack, null)
        UpdateGPS()
    }

    fun UpdateGPS() {
        // fuseLoc = LocationServices.getFusedLocationProviderClient(this)

        checkPermission()

        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
            //TODO: UI updates.

        }
    }

    /*
    private fun fn_getlocation() {
        var locationManager =
            applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        var isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        var isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


        if (!isGPSEnable && !isNetworkEnable) {
        } else {
            /*
            if (isNetworkEnable) {
                var location = null
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    0,
                    this
                )
                if (locationManager != null) {
                    location =
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (location != null) {
                        Log.e("latitude", location.getLatitude().toString() + "")
                        Log.e("longitude", location.getLongitude().toString() + "")
                        latitude = location.getLatitude()
                        longitude = location.getLongitude()
                        fn_update(location)
                    }
                }
            }

             */
            if (isGPSEnable) {
                var loc: Location?


                locationManager.requestLocationUpdates(LocReq, LocCallBack, null)

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100,
                    object : LocationListener {
                        fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                        fun onProviderEnabled(provider: String?) {}
                        fun onProviderDisabled(provider: String?) {}
                        override fun onLocationChanged(location: Location?) {}
                    })


                //     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locListener)
                if (locationManager != null) {
                    checkPermission()

                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (loc != null) {
                        Log.e("latitude", loc.getLatitude().toString() + "")
                        Log.e("longitude", loc.getLongitude().toString() + "")
                        var latitude = loc.getLatitude()
                        var longitude = loc.getLongitude()

                        /*
                        Longitude.add(loc.getLongitude())
                        Latitude.add(loc.getLatitude())



                        JArrayLat.put(loc.getLatitude())
                        JArrayLon.put(loc.getLongitude())

                        JObject.put("Latitude", JArrayLat)
                        JObject.put("Longitude", JArrayLon)

                        saveToStorage("Coords", JObject)

                         */


                        // fn_update(location)
                    }
                }
            }
        }
    }

     */

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind()")

        // MainActivity (client) comes into foreground and binds to service, so the service can
        // become a background services.
        // stopForeground(true)
        val notification = createNotificationService()
        startForeground(2003, notification)
        //serviceRunningInForeground = false
        // configurationChange = false
        return localBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "onUnbind()")

        // MainActivity (client) leaves foreground, so service needs to become a foreground service
        // to maintain the 'while-in-use' label.
        // NOTE: If this method is called due to a configuration change in MainActivity,
        // we do nothing.

        Log.d(TAG, "Start foreground service")

        // serviceRunningInForeground = true


        // Ensures onRebind() is called if MainActivity (client) rebinds.
        return true
    }

    private fun createNotificationService(): Notification {

        var notTitle = "Project Cytosine (Location)"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            var notChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                notTitle,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(notChannel)
        }

        val styles = NotificationCompat.BigTextStyle().setBigContentTitle(notTitle)
            .bigText("")


        // 3. Set up main Intent/Pending Intents for notification.
        val launchActivityIntent = Intent(this, MainPage::class.java)

        val cancelIntent = Intent(this, GPSService::class.java)
        cancelIntent.putExtra(
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION",
            true
        )

        val servicePendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, launchActivityIntent, 0
        )

        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)


        return notificationCompatBuilder
            .setStyle(styles)
            .setContentTitle(notTitle)
            .setContentText("")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCategory(Notification.CATEGORY_SERVICE)
            .addAction(
                R.mipmap.ic_launcher_round, "Open App",
                activityPendingIntent
            )
            .addAction(
                R.mipmap.ic_launcher_round,
                "Stop Tracking",
                servicePendingIntent
            )
            .build()

    }


    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
    }

    fun loadJsonFile(Filename: String): String {
        var json = ""
        try {
            val Inp: InputStream = this.openFileInput(Filename)
            val size = Inp.available()
            val buffer = ByteArray(size)
            Inp.read(buffer)
            Inp.close()
            json = String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }

    fun saveToStorage(fileName: String, jsonObj: JSONObject) {
        try {
            val writer = OutputStreamWriter(
                openFileOutput(
                    fileName + ".json",
                    Context.MODE_PRIVATE
                )
            )
            writer.write(jsonObj.toString())
            writer.close()
            // Message.setText("Saved A File Succesfully")
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }

}






