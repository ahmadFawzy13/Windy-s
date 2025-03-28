package com.example.windy.alarm.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.windy.NavigationRoute
import com.example.windy.SharedCityName
import com.example.windy.utils.NavBar
import java.util.Calendar
import kotlin.math.log


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(navController: NavController){

    var showTimePicker by remember {mutableStateOf(false)}
    val selectedTimes = remember { mutableStateListOf<TimePickerState>() }
    var cityName by remember { mutableStateOf("") }
    cityName = SharedCityName.cityName




    Scaffold(
        bottomBar = { NavBar(navController) },
        containerColor = Color(0xFF182354),
        floatingActionButton = {
            ExtendedFloatingActionButton(
            onClick = {showTimePicker = true},
            icon = { Icon(Icons.Filled.LocationOn, "Alarm") },
            text = {
                Text(text = "Set Alarm", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            },
            containerColor = Color.White
        )}

    )
    {contentPadding ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding))
        {


            LazyColumn{
                items (selectedTimes.size) {
                    AlarmsList(selectedTimes[it],cityName)
                }
            }

            if(showTimePicker){
                SetAlarm(onConfirm = { alarm->

                    val duplicateAlarm = selectedTimes.any{
                        it.hour == alarm.hour && it.minute == alarm.minute
                    }

                    if(!duplicateAlarm){
                        selectedTimes.add(alarm)
                    }

                    showTimePicker = false
                },
                    onDismiss = {
                        showTimePicker = false
                    })
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmsList(alarmTime:TimePickerState,cityName:String){

    Card(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    )
    {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Icon(imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier.clickable{

                })

            Spacer(modifier = Modifier.width(15.dp))

            Column ()
            {

                Text(
                    text = ("${alarmTime.hour}:${alarmTime.minute}"),
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.width(10.dp))


                Text(text = "Alarm",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.padding(10.dp ))

            Text(
                text = cityName,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetAlarm(
    onConfirm: (TimePickerState) -> Unit = { timeState->},
    onDismiss: () -> Unit = {}
) {
    val currentTime = Calendar.getInstance()
    val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
    val currentMinute = currentTime.get(Calendar.MINUTE)

    val timePickerState = rememberTimePickerState(
        initialHour = currentHour,
        initialMinute = currentMinute
    )

    val isInvalidTime = (timePickerState.hour < currentHour ||
            (timePickerState.hour == currentHour && timePickerState.minute < currentMinute))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF182354)),
        contentAlignment = Alignment.Center,


    ){

        Column(horizontalAlignment = Alignment.CenterHorizontally){
            TimePicker(
                state = timePickerState,
            )

            Button(onClick =
                {onConfirm(timePickerState)},
                enabled =!isInvalidTime,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            )
            {
                Text("Confirm",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.padding(10.dp))

            Button(onClick = onDismiss,
                   colors = ButtonDefaults.buttonColors(
                       containerColor = Color.White,
                       contentColor = Color.Black
                   )
                )
            {
                Text("Dismiss",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp)
            }
        }
    }
}

