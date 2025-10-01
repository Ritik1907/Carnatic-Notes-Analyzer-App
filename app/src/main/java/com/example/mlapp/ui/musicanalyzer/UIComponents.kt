package com.example.mlapp.ui.musicanalyzer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AudioUploadButton(
    modifier: Modifier = Modifier,
    onAudioSelected: (Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onAudioSelected(it) }
    }

    OutlinedButton(
        onClick = { launcher.launch("audio/*") },
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        border = BorderStroke(2.dp, Color(0xFF6C63FF)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Upload,
            contentDescription = "Upload",
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "Choose Audio File",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun GlassyHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0x336C63FF),
                        Color(0x3300d4aa)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun NoteCard(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213e)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00d4aa),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = content,
                fontSize = 14.sp,
                color = Color(0xFFe0e0e0),
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun ScaleIndicator(scale: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF6C63FF),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Detected Scale",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = scale,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// Updated CarnaticNotesScreen with better UI
//@Composable
//fun ImprovedCarnaticNotesScreen(navController: androidx.navigation.NavController) {
//    var selectedAudio by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<Uri?>(null) }
//    var detectedNotes by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<List<CarnaticNote>>(emptyList()) }
//    var detectedScale by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
//    var isProcessing by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
//    val context = androidx.compose.ui.platform.LocalContext.current
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF0f0c29),
//                        Color(0xFF302b63),
//                        Color(0xFF24243e)
//                    )
//                )
//            )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(20.dp)
//        ) {
//            GlassyHeader("🎵 Carnatic Notes Analyzer")
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            AudioUploadButton(
//                modifier = Modifier.fillMaxWidth(),
//                onAudioSelected = { uri ->
//                    selectedAudio = uri
//                }
//            )
//
//            selectedAudio?.let { uri ->
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color(0x22ffffff)
//                    ),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Column(modifier = Modifier.padding(12.dp)) {
//                        Text(
//                            text = "📁 ${uri.lastPathSegment ?: "Audio file"}",
//                            color = Color.White,
//                            fontSize = 14.sp
//                        )
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        Button(
//                            onClick = {
//                                isProcessing = true
//                                analyzeAudio(context, uri) { notes, scale ->
//                                    detectedNotes = notes
//                                    detectedScale = scale
//                                    isProcessing = false
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(0xFF00d4aa)
//                            ),
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Text(
//                                "🎼 Analyze & Extract Notes",
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.SemiBold
//                            )
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            when {
//                isProcessing -> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1f),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.size(60.dp),
//                                color = Color(0xFF6C63FF),
//                                strokeWidth = 6.dp
//                            )
//                            Spacer(modifier = Modifier.height(16.dp))
//                            Text(
//                                "Analyzing audio...",
//                                color = Color.White,
//                                fontSize = 16.sp
//                            )
//                        }
//                    }
//                }
//                detectedNotes.isNotEmpty() -> {
//                    Column(
//                        modifier = Modifier
//                            .weight(1f)
//                            .verticalScroll(androidx.compose.foundation.rememberScrollState())
//                    ) {
//                        detectedScale?.let {
//                            ScaleIndicator(it)
//                            Spacer(modifier = Modifier.height(16.dp))
//                        }
//
//                        NoteCard(
//                            title = "🎶 Sargam Notation (Carnatic)",
//                            content = formatNotesAsSargam(detectedNotes),
//                            modifier = Modifier.fillMaxWidth()
//                        )
//
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        NoteCard(
//                            title = "🎹 Western Notation",
//                            content = formatNotesAsWestern(detectedNotes),
//                            modifier = Modifier.fillMaxWidth()
//                        )
//
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        NoteCard(
//                            title = "📊 Note Statistics",
//                            content = "Total notes: ${detectedNotes.size}\n" +
//                                    "Duration: ${String.format("%.1f", detectedNotes.sumOf { it.duration.toDouble() })}s",
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//                }
//                else -> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1f),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            "Upload an audio file to get started",
//                            color = Color.White.copy(alpha = 0.6f),
//                            fontSize = 16.sp
//                        )
//                    }
//                }
//            }
//        }
//    }
//}