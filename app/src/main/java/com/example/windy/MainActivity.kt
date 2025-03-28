package com.example.windy

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.windy.alarm.view.AlarmScreen
import com.example.windy.alarm.view.MyAlarmFactory
import com.example.windy.alarm.view.SetAlarm
import com.example.windy.data.repo.Repository
import com.example.windy.favourite.view.FavouriteScreen
import com.example.windy.favourite.view.MapScreen
import com.example.windy.favourite.viewmodel.MyFavFactory
import com.example.windy.home.view.HomeScreen
import com.example.windy.home.viewmodel.MyHomeFactory

const val REQUEST_LOCATION_CODE = 134
const val REQUEST_NOTIFICATIONS_CODE = 567
class MainActivity : ComponentActivity() {

    var currentLocation = mutableStateOf<String?>(null) //3shan bytghyr brdo
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient //byrg3 location
    lateinit var locationState: MutableState<Location> //3shan el location bytghyr

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            locationState = remember{mutableStateOf(Location(LocationManager.GPS_PROVIDER))}

            val navController = rememberNavController()
            NavHost(navController = navController,
                startDestination = NavigationRoute.Home) {

                composable<NavigationRoute.HomeWithParameters>{params->

                    val data =params.toRoute<NavigationRoute.HomeWithParameters>()
                    val favLat = data.favLat
                    val favLon = data.favLon

                    HomeScreen(navController,
                        viewModel(factory = MyHomeFactory(Repository.getInstance(this@MainActivity))),
                        locationState.value,
                        "metric",favLat,favLon)
                }

                composable <NavigationRoute.Home>{
                    HomeScreen(navController,
                        viewModel(factory = MyHomeFactory(Repository.getInstance(this@MainActivity))),
                        locationState.value,"metric")
                }

                composable <NavigationRoute.Favourite>{
                    FavouriteScreen(navController,viewModel(factory = MyFavFactory(Repository.getInstance(this@MainActivity))))
                }

                composable<NavigationRoute.Map> {
                    MapScreen(viewModel(factory = MyFavFactory(Repository.getInstance(this@MainActivity))))
                }

                composable <NavigationRoute.Alarm> {
                    AlarmScreen(navController,viewModel(factory = MyAlarmFactory(Repository.getInstance(this@MainActivity))))
                }

                composable <NavigationRoute.SetAlarm> {
                    SetAlarm()
                }
            }
        }
    }

    ///////////Gps&Notification Code
    override fun onStart() {
        super.onStart()

        when{
            !checkGpsPermissions() -> requestGpsPermissions()
            !isLocationEnabled() -> enableLocationServices()
            else -> getFreshLocation()
        }

        when{
            !checkNotificationPermission() -> requestNotificationsPermissions()
            !isAlarmsAndRemindersEnabled() -> enableAlarmsAndReminders()
        }
    }

    private fun checkGpsPermissions(): Boolean {
        var result=false
        if ((ContextCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            ||
            (ContextCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
        {
            result=true
        }
        return result
    }

    private fun requestGpsPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_LOCATION_CODE
        )
    }

    private fun isLocationEnabled():Boolean{
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),

            object : LocationCallback(){
                override fun onLocationResult(location : LocationResult) {
                    super.onLocationResult(location)
                    locationState.value = location.lastLocation?: Location(LocationManager.GPS_PROVIDER)
                    geocoderLocation(locationState.value,currentLocation)
                }
            },
            Looper.myLooper()
        )
    }

    private fun geocoderLocation(location: Location, currentLocation: MutableState<String?>) {
        val geocoder = Geocoder(this)

        geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1,
            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: List<Address?>) {
                    if (addresses.isNotEmpty()) {
                        currentLocation.value = addresses[0]?.getAddressLine(0)
                    }
                }
            })
    }

    fun enableLocationServices(){
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, // the request code I gave
        permissions: Array<out String?>, // the array I saved the permissions in
        grantResults: IntArray, //when user grants or revokes gives a value
        deviceId: Int // not used
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)

        if(requestCode == REQUEST_LOCATION_CODE){
            if(grantResults.isNotEmpty() &&(grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                if(isLocationEnabled()){

                    getFreshLocation()

                }else{
                    enableLocationServices()
                }
            }else{
                Log.i("TAG", "onRequestPermissionsResult: location denied")
            }

        }else if(requestCode == REQUEST_NOTIFICATIONS_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i("TAG", "onRequestPermissionsResult: permission granted")
            }else{
                Log.i("TAG", "onRequestPermissionsResult: permission denied")
            }
        }
    }

    private fun checkNotificationPermission(): Boolean {
        var result = false
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            result = true
        }
        return result
    }
    private fun requestNotificationsPermissions(){

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            REQUEST_NOTIFICATIONS_CODE)

    }
    private fun isAlarmsAndRemindersEnabled():Boolean{

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return alarmManager.canScheduleExactAlarms()

    }
    private fun enableAlarmsAndReminders(){
        val intent = Intent (Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        startActivity(intent)
    }
}
