package com.example.mlapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mlapp.ui.theme.MlappTheme


@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Welcome To App!")

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ElevatedButton(
                onClick = { navController.navigate("settings") },
                modifier = Modifier
                    .weight(1f) // equal share of width
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp,
                    focusedElevation = 12.dp
                )
            ) {
                Text(
                    text = "Navigate",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                )
            }

            ElevatedButton(
                onClick = { navController.navigate("permission") },
                modifier = Modifier
                    .weight(1f) // equal share of width
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Camera", fontSize = 18.sp)
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            ElevatedButton(
                onClick = { navController.navigate("musicNodes") },
                modifier = Modifier
                    .weight(1f) // equal share of width
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White

                )
            ) {
                Text(
                    text = "Musical Nodes",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                )

            }
            ElevatedButton(
                onClick = { navController.navigate("carnaticNotesScreen") },
                modifier = Modifier
                    .weight(1f) // equal share of width
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White

                )
            ) {
                Text(
                    text = "Musical Nodes",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                )

            }
        }

        fun test(text: String) {
            println("I print what i want")
        }

        val lambda: ((String) -> Unit)? = null


        val count : Int = 10

        doSomething("", ::test)
    }
}


fun doSomething(text: String, doingSomething: (String) -> Unit) {
    println(text)
    doingSomething("Running")
    doingSomething("100")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MlappTheme {
        HomeScreen(
            navController = NavController(LocalContext.current)
        )
    }
}