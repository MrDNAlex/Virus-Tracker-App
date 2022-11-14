package com.example.projectcytosinevirusigotchatracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts.Settings.getSetting
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class AccountSettings : AppCompatActivity() {

    private lateinit var settingsGridView: GridView
    private lateinit var Frequency: String
    private lateinit var Accuracy: String
    private lateinit var Language: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)



        settingsGridView = findViewById(R.id.AccountGridView)

        settingsGridView.adapter = SettingsGridViewAdapter(this, 3, false, "Frequency")

    }

    fun getSettingsInfo() {
        try {
            var InfoFile = JSONObject(loadJsonFile("Settings" + ".json"))
            var settSpeed = InfoFile.getJSONArray("Frequency")
            var settAccuracy = InfoFile.getJSONArray("Accuracy")
            var settLanguage = InfoFile.getJSONArray("Language")

            Frequency = settSpeed[0].toString()
            Accuracy = settAccuracy[0].toString()
            Language = settLanguage[0].toString()
        } catch (e: JSONException) {
            e.printStackTrace();
        }
    }

    fun getSetting(pos: Int): String {

        var message = ""
        when (pos) {

            0 -> {
                //Frequency
                var Time = (Frequency.toInt() / 1000) / 60
                if (Time == 1) {
                    message = "Once every $Time Min"
                } else {
                    message = "Once every $Time Mins"
                }
            }
            1 -> {
                //Accuracy
                if (Accuracy.equals("HIGH")) {
                    message = "High"
                } else {
                    message = "Low"
                }
            }
            2 -> {
                //Language
                message = Language
            }
        }
        return message
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


    private class SettingsGridViewAdapter(var context: Context, var length: Int, var Edit:Boolean, var EditType : String) : BaseAdapter() {

        private lateinit var settSelected: TextView
        private lateinit var settTitle: TextView
        private lateinit var settBar: LinearLayout
        private var accSett = AccountSettings()

        private var devWidth = 0
        private var devHeight = 0


        fun loadJsonFile(Filename: String): String {
            var json = ""
            try {
                val Inp: InputStream = context.openFileInput(Filename)
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



        override fun getCount(): Int {
            return length
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var view: View = View.inflate(context, R.layout.account_header, null)

            if (Edit) {
                when (EditType) {
                    "Frequency" -> {
                        view = View.inflate(context, R.layout.settings_item, null)
                        settBar = view.findViewById(R.id.SettingsBar)
                        settTitle = view.findViewById(R.id.SettingTitle)
                        settSelected = view.findViewById(R.id.SettingSelected)


                        if (position == 0) {
                            settTitle.setText("Frequency")
                            settSelected.setText("")
                        } else {
                            settTitle.setText("")
                            settSelected.setText("")
                        }


                    }
                    "Accuracy" -> {

                    }
                    "Language" -> {

                    }
                }
            } else {
                when (position) {

                    0 -> {
                        //Frequency
                        view = View.inflate(context, R.layout.settings_item, null)
                        settBar = view.findViewById(R.id.SettingsBar)
                        settTitle = view.findViewById(R.id.SettingTitle)
                        settSelected = view.findViewById(R.id.SettingSelected)

                        settTitle.setText("Frequency")
                        settSelected.setText(accSett.getSetting(position))

                    }
                    1 -> {
                        view = View.inflate(context, R.layout.settings_item, null)
                        settBar = view.findViewById(R.id.SettingsBar)
                        settTitle = view.findViewById(R.id.SettingTitle)
                        settSelected = view.findViewById(R.id.SettingSelected)

                        settTitle.setText("Accuracy")
                        settSelected.setText(accSett.getSetting(position))
                    }
                    2 -> {
                        view = View.inflate(context, R.layout.settings_item, null)
                        settBar = view.findViewById(R.id.SettingsBar)
                        settTitle = view.findViewById(R.id.SettingTitle)
                        settSelected = view.findViewById(R.id.SettingSelected)

                        settTitle.setText("Language")
                        settSelected.setText(accSett.getSetting(position))
                    }
                }
            }

            return view
        }
    }
}