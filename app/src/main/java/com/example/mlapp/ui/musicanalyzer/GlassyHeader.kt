//package com.example.mlapp.ui.musicanalyzer
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.blur
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun GlassyHeader(title: String) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        // 🔹 Background (blurred glass layer)
//        Box(
//            modifier = Modifier
//                .matchParentSize()
//                .clip(RoundedCornerShape(24.dp))
//                .background(
//                    Brush.linearGradient(
//                        listOf(
//                            Color.White.copy(alpha = 0.3f),
//                            Color.White.copy(alpha = 0.1f)
//                        )
//                    )
//                )
//                .blur(20.dp) // only background is blurred
//                .border(
//                    width = 1.dp,
//                    brush = Brush.linearGradient(
//                        listOf(
//                            Color.White.copy(alpha = 0.8f),
//                            Color.White.copy(alpha = 0.2f)
//                        )
//                    ),
//                    shape = RoundedCornerShape(24.dp)
//                )
//        )
//        Text(
//            text = title,
//            color = Color.Black,
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
//        )
//    }
//}
//
//
//@Preview
//@Composable
//fun GlassyHeaderPreview(){
//    GlassyHeader("Music Nodes ML Model")
//}
//
