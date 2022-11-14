package com.example.projectcytosinevirusigotchatracker

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter
import java.security.AccessController.getContext
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var LoginButton: Button
    lateinit var RegisterButton: Button

    /*val android_id = Settings.Secure.getString(
        this.contentResolver,
        Settings.Secure.ANDROID_ID
    )

     */



    val android_id = null

    val account_id = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RegisterButton = findViewById(R.id.RegisterButton)
        LoginButton = findViewById(R.id.LoginButton)

        RegisterButton.setOnClickListener({



            signUp()
        })

        LoginButton.setOnClickListener({
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                1
            )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                1
            )

            verifyLogin()

        })


    }

    fun verifyLogin() {
        val UsernameLogin: EditText = findViewById(R.id.LoginEmail)
        val PasswordLogin: EditText = findViewById(R.id.LoginPassword)
        var Username: String = UsernameLogin.toString()
        var Password: String = PasswordLogin.toString()

        //Eventually have it send the data on the internet/to the home server

    }

    fun signUp() {
        //Currently skip so send to next activity but switch it to a login page eventually
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.FOREGROUND_SERVICE), 1)
        var hi = LocationServices.getFusedLocationProviderClient(this)

        var SignUpPage: Intent = Intent(applicationContext, MainPage::class.java).apply { }
        startActivity(SignUpPage)

        try {
            val InpStr: InputStream = openFileInput("Settings.json")
        } catch (e: FileNotFoundException) {
            genSettings()
        }


    }

    fun genSettings() {

        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm.getDefaultDisplay()
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels



        var devWidth = JSONArray()
        devWidth.put(width)
        var devHeight = JSONArray()
        devHeight.put(height)

        var devID = JSONArray()
        devID.put(android_id)
        var accID = JSONArray()
        accID.put(account_id)


        var jObjSettings = JSONObject()

        //Interval is 1000 = 1 sec
        //Call default for every 5 mins with 4 settings? 1 min, 5 mins, 10 mins, 20 mins and 30 mins?


        var callSpeed = 1000 * 60 * 1
        var locAccuracy = "HIGH"
        var language: String = Locale.getDefault().getDisplayLanguage()
        var accImage = ""
        var accFirstName = "Name"
        var accLastName = "Name Name"
        var varNull = 0

        var jSpeed = JSONArray() //Location callback speed
        var jAccuracy = JSONArray() //High Accuracy or low
        var jLang = JSONArray() //App Language
        var jImage = JSONArray() //Account Image
        var jFirstName = JSONArray() //First Name Account
        var jLastName = JSONArray() //Last Name Account
        var jImageX = JSONArray() //Image start position (for when telling where to grab from)
        var jImageY = JSONArray() //Image start position (for when telling where to grab from)
        var jImageHeight = JSONArray()
        var jImageWidth = JSONArray()

        jSpeed.put(callSpeed)
        jAccuracy.put(locAccuracy)
        jLang.put(language)
        jImage.put(accImage)
        jFirstName.put(accFirstName)
        jLastName.put(accLastName)

        jImageX.put(varNull)
        jImageY.put(varNull)
        jImageWidth.put(varNull)
        jImageHeight.put(varNull)


        jObjSettings.put("Frequency", jSpeed)
        jObjSettings.put("Accuracy", jAccuracy)
        jObjSettings.put("Language", jLang)
        jObjSettings.put("AccountImage", jImage)
        jObjSettings.put("AccountImageX", jImage)
        jObjSettings.put("AccountImageY", jImage)
        jObjSettings.put("AccountImageWidth", jImage)
        jObjSettings.put("AccountImageHeight", jImage)

        jObjSettings.put("AccountFirstName", jFirstName)
        jObjSettings.put("AccountLastName", jLastName)
        
        jObjSettings.put("DeviceID", devID)
        jObjSettings.put("AccountID", accID)

        jObjSettings.put("DeviceWidth", devWidth)
        jObjSettings.put("DeviceHeight", devHeight)



        saveToStorage("Settings", jObjSettings)

        //
        //Upload File
        //
        var UploadFile = JSONObject()
        var UploadLocation = JSONArray()

        UploadFile.put("Locations", UploadLocation)
        UploadFile.put("DeviceID", devID)
        UploadFile.put("AccountID", accID)

        //
        //Week File
        //
        var WeekFile = JSONObject()
        var WeekLocation = JSONArray()

        WeekFile.put("Locations",WeekLocation)
        WeekFile.put("DeviceID", devID)
        WeekFile.put("AccountID", accID)

        //
        //Master File
        //
        var MasterFile = JSONObject()
        var MasterLocation = JSONArray()

        MasterFile.put("Locations", MasterLocation)
        MasterFile.put("DeviceID", devID)
        MasterFile.put("AccountID", accID)

        //
        //Emergency File
        //
        var EmergencyFile = JSONObject()
        var EmergencyLocation = JSONArray()

        EmergencyFile.put("Locations", EmergencyLocation)
        EmergencyFile.put("DeviceID", devID)
        EmergencyFile.put("AccountID", accID)


        saveToStorage("UploadLocation", UploadFile)
        saveToStorage("WeekLocation", WeekFile)
        saveToStorage("MasterLocation", MasterFile)
        saveToStorage("EmergencyLocation", EmergencyFile)

    }


    fun saveToStorage(fileName: String, jsonObj: JSONObject) {
        try {
            val writer = OutputStreamWriter(
                openFileOutput(
                    fileName + ".json",
                    MODE_PRIVATE
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