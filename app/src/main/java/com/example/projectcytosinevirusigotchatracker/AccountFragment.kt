package com.example.projectcytosinevirusigotchatracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
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
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var accountGridView: GridView

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

        var view = inflater.inflate(R.layout.fragment_account, container, false)

        //So set up a grid view
        //have a settings bar, profile, personal info, account image/ account desc at top, family/friends
        //I guess for accounts let's make custom images for now and maybe one day add a custom image, but you can upload your images

        accountGridView = view.findViewById(R.id.AccountGridView)

        accountGridView.adapter = AccountGridViewAdapter(requireContext(),4)



        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

   public fun goToSettings () {
        var SettingsPage : Intent = Intent(context, AccountSettings::class.java).apply { }
        startActivity(SettingsPage)
    }

    fun goToAccountInfo () {
        var SettingsPage : Intent = Intent(context, AccountSettings::class.java).apply { }
        startActivity(SettingsPage)
    }

    fun goToData () {
        var SettingsPage : Intent = Intent(context, AccountSettings::class.java).apply { }
        startActivity(SettingsPage)
    }


    private class AccountGridViewAdapter(var context: Context, var length: Int) : BaseAdapter() {


        private lateinit var accBar: LinearLayout
        private lateinit var accCardView: CardView
        private lateinit var accImg: ImageView
        private lateinit var accFirstName: TextView
        private lateinit var accLastName: TextView
        private lateinit var barText: TextView

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


            when (position) {

                0 -> {
                    try {
                        var InfoFile = JSONObject(loadJsonFile("Settings" + ".json"))
                        var Width = InfoFile.getJSONArray("DeviceWidth")
                        var Height = InfoFile.getJSONArray("DeviceHeight")
                        devWidth = Width[0] as Int
                        devHeight = Height[0] as Int

                    }  catch (e : JSONException) {
                        e.printStackTrace();
                    }

                    view = View.inflate(context, R.layout.account_header, null)

                    accCardView = view.findViewById(R.id.accountCardView)
                    accImg = view.findViewById(R.id.accountImage)
                    accFirstName = view.findViewById(R.id.accountFirstName)
                    accLastName = view.findViewById(R.id.accountLastName)

                    accImg.layoutParams.width = (devWidth * 0.8).toInt()
                    accImg.layoutParams.height = (devWidth * 0.8).toInt()
                    accCardView.radius = ((devWidth * 0.8) / 2).toFloat()

                    accFirstName.setText("Mr DNA")
                    accLastName.setText("Test Account")

                }

                1 -> {
                    //Settings
                    view = View.inflate(context, R.layout.account_bar, null)
                    accBar = view.findViewById(R.id.accountBar)
                    barText = view.findViewById(R.id.barText)
                    barText.setText(R.string.AccSettings)

                    accBar.setOnClickListener {
                        val accFrag = AccountFragment()

                        accFrag.goToSettings()

                    }

                }

                2 -> {
                    //Account Info
                    view = View.inflate(context, R.layout.account_bar, null)
                    accBar = view.findViewById(R.id.accountBar)
                    barText = view.findViewById(R.id.barText)
                    barText.setText(R.string.AccInfo)
                }


                3 -> {
                    //Data
                    view = View.inflate(context, R.layout.account_bar, null)
                    accBar = view.findViewById(R.id.accountBar)
                    barText = view.findViewById(R.id.barText)
                    barText.setText(R.string.AccData)
                }


                4 -> {
                    //Data?

                }


            }

            return view

        }


    }

}