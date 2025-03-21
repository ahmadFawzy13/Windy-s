package com.example.windy

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.windy.data.repo.Repository
import com.example.windy.favourite.view.FavouriteScreen
import com.example.windy.favourite.viewmodel.MyFavFactory
import com.example.windy.home.view.HomeScreen
import com.example.windy.home.viewmodel.HomeViewModel
import com.example.windy.home.viewmodel.MyHomeFactory
import com.example.windy.ui.theme.WindyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val REQUEST_LOCATION_CODE = 134
class MainActivity : ComponentActivity() {

    var currentLocation = mutableStateOf<String?>(null) //3shan bytghyr brdo
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient //byrg3 location
    lateinit var locationState: MutableState<Location> //3shan el location bytghyr

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            locationState = remember{mutableStateOf(Location(LocationManager.GPS_PROVIDER))}
            val navController = rememberNavController()

            NavHost(navController = navController,
                startDestination = NavigationRoute.Home) {

                composable<NavigationRoute.Home>{
                    HomeScreen(navController,viewModel(factory = MyHomeFactory(Repository.getInstance(this@MainActivity))))
                }

                composable <NavigationRoute.Favourite>{
                    FavouriteScreen(navController,viewModel(factory = MyFavFactory(Repository.getInstance(this@MainActivity))))
                }

            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val repo = Repository.getInstance(this@MainActivity)
            val result = repo.getCurrentWeatherRemote("41.3874","2.1686","metric")
            val resultTwo = repo.getFiveDayThreeHourWeatherRemote("41.3874","2.1686","metric")
            Log.i("TAG", "onCreate: $result")
            Log.i("TAG", "onCreate: $resultTwo")
            Log.i("TAG", "onCreate: ${resultTwo?.city}")
        }
    }

    override fun onStart() {
        super.onStart()
        if(checkPermissions()){
            if(isLocationEnabled()){
                getFreshLocation()
            }else{
                enableLocationServices()
            }
        }else{
            //da hytl3 pop up 3shan a3rf el user das 3la eh h3ml override callback method esmaha onRequestPermissionsResult
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_CODE
            )
        }
    }

    fun checkPermissions(): Boolean {
        var result=false
        if ((ContextCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            ||
            (ContextCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
        {
            result=true
        }
        return result
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
        Toast.makeText(this,"Turn on Location", Toast.LENGTH_SHORT).show()
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
            if(grantResults.get(0) == PackageManager.PERMISSION_GRANTED || grantResults.get(1) == PackageManager.PERMISSION_GRANTED){

                if(isLocationEnabled()){

                    getFreshLocation()

                }else{
                    enableLocationServices()
                }
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    REQUEST_LOCATION_CODE
                )
            }
        }
    }
}
