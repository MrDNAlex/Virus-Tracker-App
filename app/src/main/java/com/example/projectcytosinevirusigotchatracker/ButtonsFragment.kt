package com.example.projectcytosinevirusigotchatracker

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [ButtonsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ButtonsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var StartGPS : ImageButton
    lateinit var StopGPS : ImageButton

    lateinit var fragView : View



  lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var locationMan : LocationManager

    var hasGPS = false

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment




        //    fusedLocationClient = LocationServices.getFusedLocationProviderClient(fragView.context)
        //   getLocationUpdates()

        //location = getSystemService(LOCATION_SERVICE) as LocationManager?





        /*
        StartGPS.setOnClickListener(View.OnClickListener { view ->
            var GPS: Intent = Intent(context, GPSService::class.java).apply { }
            context?.startService(GPS)
        })

        StopGPS.setOnClickListener(View.OnClickListener { view ->

        })

        StartGPS.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

            }

        })




        StopGPS.setOnClickListener({
            // stopLocationUpdates()
        })

         */

      //  return inflater.inflate(R.layout.fragment_buttons, container, false)

        fragView = inflater.inflate(R.layout.fragment_buttons, container, false)

        StartGPS = fragView.findViewById(R.id.StartGPS)
        StopGPS = fragView.findViewById(R.id.StopGPS)

        StartGPS.setOnClickListener {
            var GPS: Intent = Intent(requireContext(), GPSService::class.java).apply { }
            requireContext().startService(GPS)
        }

        return fragView


    }


    companion object {


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ButtonsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ButtonsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

/*
    private fun getLocationUpdates()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(fragView.context)
        locationRequest = LocationRequest()
        locationRequest.interval = 200
        locationRequest.fastestInterval = 200
        locationRequest.smallestDisplacement = 0.5f // 170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location =
                        locationResult.lastLocation
                    // use your location object
                    // get latitude , longitude and other info from this
                    var hi = 0
                }

            }
        }
    }

    //start location updates
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    // start receiving location update when activity  visible/foreground
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

 */
}


