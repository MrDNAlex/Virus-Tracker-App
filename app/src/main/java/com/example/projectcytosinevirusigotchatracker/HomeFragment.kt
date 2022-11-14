package com.example.projectcytosinevirusigotchatracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var LocationGridView: GridView

    //  lateinit var textBut : Button
//lateinit var MesLog : TextView
//lateinit var fragView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var textBut: Button
        var MesLog: GridView
        var fragView: View

        fragView = inflater.inflate(R.layout.fragment_home, container, false)

        MesLog = fragView.findViewById(R.id.LocationLog)

        textBut = fragView.findViewById(R.id.ButtonText)

        textBut.setOnClickListener {

            LocationGridView = fragView.findViewById(R.id.LocationLog)

            LocationGridView.adapter = LocationGridViewAdapter(requireContext())
            try {
                var InfoFile = JSONObject(loadJsonFile("Coords" + ".json"))
                var Locations = InfoFile.getJSONArray("Locations")


                var message = ""


                for (i in 1..Locations.length() - 1) {

                    message += Locations[i].toString()

                }


                //MesLog.setText(message)
            } catch (e: JSONException) {
                e.printStackTrace();
            }
        }

        // Inflate the layout for this fragment
        return fragView
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun loadJsonFile(Filename: String): String {
        var json = ""
        try {
            val Inp: InputStream = requireContext().openFileInput(Filename)
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


    private class LocationGridViewAdapter(var context: Context) : BaseAdapter() {

        lateinit var InfoFile: JSONObject
        lateinit var Locations: JSONArray


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
            try {
                InfoFile = JSONObject(loadJsonFile("Coords" + ".json"))
                Locations = InfoFile.getJSONArray("Locations")

                //MesLog.setText(message)
            } catch (e: JSONException) {
                e.printStackTrace();
            }

            return Locations.length()
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var view: View = View.inflate(context, R.layout.location_bar, null)

            var LocationInfo: TextView

            LocationInfo = view.findViewById(R.id.LocationInfo)

            LocationInfo.setText(Locations[position].toString())

            return view
        }
    }

}