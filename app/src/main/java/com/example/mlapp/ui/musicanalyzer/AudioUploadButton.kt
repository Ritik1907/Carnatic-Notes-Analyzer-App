//package com.example.mlapp.ui.musicanalyzer
//
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.UploadFile
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun AudioUploadButton(
//    modifier: Modifier = Modifier,
//    onAudioSelected: (Uri) -> Unit   // 👈 callback function
//) {
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let { onAudioSelected(it) } // calls the callback
//    }
//
//    Button(
//        onClick = { launcher.launch("audio/*") },
//        shape = RoundedCornerShape(16.dp),
//        modifier = modifier,
//        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
//    ) {
//        Icon(
//            imageVector = Icons.Default.UploadFile,
//            contentDescription = "Upload Audio",
//            tint = Color.White
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text("Upload Audio", color = Color.White)
//    }
//}
