package com.example.windy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.windy.data.repo.Repository
import com.example.windy.home.viewmodel.HomeViewModel
import com.example.windy.home.viewmodel.MyHomeFactory
import com.example.windy.ui.theme.WindyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }

        /*lifecycleScope.launch(Dispatchers.IO) {
            val repo = Repository.getInstance(this@MainActivity)
            val result = repo.getRemoteWeather("41.3874","2.1686","metric")
            val resultTwo = repo.getRemoteFiveDayThreeHourWeather("41.3874","2.1686","metric")
            Log.i("TAG", "onCreate: $result")
            Log.i("TAG", "onCreate: $resultTwo")
            Log.i("TAG", "onCreate: ${resultTwo?.city}")
        }*/
    }
}
