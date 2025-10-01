import androidx.compose.runtime.Composable
import androidx.navigation.NavController

//package com.example.mlapp.ui.musicanalyzer
//
//import android.content.Context
//import android.media.MediaCodec
//import android.media.MediaExtractor
//import android.media.MediaFormat
//import android.net.Uri
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import be.tarsos.dsp.AudioDispatcher
//import be.tarsos.dsp.io.TarsosDSPAudioFormat
//import be.tarsos.dsp.io.UniversalAudioInputStream
//import be.tarsos.dsp.pitch.PitchDetectionHandler
//import be.tarsos.dsp.pitch.PitchDetectionResult
//import be.tarsos.dsp.pitch.PitchProcessor
//import com.example.mlapp.ui.theme.RoseGradient
//import java.io.File
//import java.io.FileOutputStream
//import java.io.RandomAccessFile
//import java.nio.ByteBuffer
//
//@Composable
//fun MusicNodesScreen(navController: NavController) {
//    var selectedAudio by remember { mutableStateOf<Uri?>(null) }
//    var detectedPitches by remember { mutableStateOf<List<Float>>(emptyList()) }
//    var isProcessing by remember { mutableStateOf(false) }
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(RoseGradient)
//            .padding(top = 30.dp, start = 10.dp, end = 10.dp),
//        verticalArrangement = Arrangement.Top
//    ) {
//        GlassyHeader("Music Nodes ML Model")
//        Spacer(modifier = Modifier.height(24.dp))
//
//        AudioUploadButton(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.CenterHorizontally),
//            onAudioSelected = { uri ->
//                selectedAudio = uri
//                isProcessing = true
//                detectPitchFromAudio(context, uri) { pitches ->
//                    detectedPitches = pitches
//                    isProcessing = false
//                }
//            }
//        )
//
//        selectedAudio?.let { uri ->
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = "Selected audio: ${uri.lastPathSegment}",
//                color = Color.White
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        when {
//            isProcessing -> Text("Processing audio for pitch detection...", color = Color.White)
//            detectedPitches.isNotEmpty() -> {
//                Text("Detected Pitches (Hz):", color = Color.White)
//                Spacer(modifier = Modifier.height(8.dp))
//                val display = detectedPitches.take(100).joinToString(", ") {
//                    if (it <= 0f) "0" else "%.1f".format(it)
//                }
//                Text(display, color = Color.White)
//            }
//        }
//    }
//}
//
//private fun uriToTempFile(context: Context, uri: Uri): File {
//    val tempFile = File.createTempFile("audio_input_", ".tmp", context.cacheDir)
//    context.contentResolver.openInputStream(uri)?.use { inputStream ->
//        FileOutputStream(tempFile).use { outputStream ->
//            inputStream.copyTo(outputStream)
//        }
//    }
//    return tempFile
//}
//
//private fun detectPitchFromAudio(
//    context: Context,
//    audioUri: Uri,
//    onResult: (List<Float>) -> Unit
//) {
//    Thread {
//        val pitchList = mutableListOf<Float>()
//        try {
//            // Decode audio using Android MediaExtractor/MediaCodec
//            val audioData = decodeAudioToPCM(context, audioUri)
//
//            if (audioData != null) {
//                val sampleRate = audioData.sampleRate
//                val audioBuffer = audioData.pcmData
//                val bufferSize = 2048
//                val bufferOverlap = 1024
//
//                // Create TarsosDSP audio format
//                val audioFormat = TarsosDSPAudioFormat(
//                    sampleRate.toFloat(),
//                    16, // 16-bit
//                    audioData.channelCount,
//                    true,
//                    false
//                )
//
//                // Convert byte array to TarsosDSP format
//                val audioInputStream = audioBuffer.inputStream()
//                val universalStream = UniversalAudioInputStream(audioInputStream, audioFormat)
//
//                val dispatcher = AudioDispatcher(universalStream, bufferSize, bufferOverlap)
//
//                val pitchHandler = PitchDetectionHandler { res: PitchDetectionResult, _ ->
//                    val pitch = res.pitch
//                    if (pitch > 0) { // Only add valid pitches
//                        pitchList.add(pitch)
//                    }
//                }
//
//                val pitchProcessor = PitchProcessor(
//                    PitchProcessor.PitchEstimationAlgorithm.YIN,
//                    sampleRate.toFloat(),
//                    bufferSize,
//                    pitchHandler
//                )
//
//                dispatcher.addAudioProcessor(pitchProcessor)
//                dispatcher.run()
//            }
//
//            onResult(pitchList)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            onResult(emptyList())
//        }
//    }.start()
//}
//
//data class AudioData(
//    val pcmData: ByteArray,
//    val sampleRate: Int,
//    val channelCount: Int
//)
//
//private fun decodeAudioToPCM(context: Context, uri: Uri): AudioData? {
//    val extractor = MediaExtractor()
//    try {
//        extractor.setDataSource(context, uri, null)
//
//        // Find audio track
//        var audioTrackIndex = -1
//        var audioFormat: MediaFormat? = null
//        for (i in 0 until extractor.trackCount) {
//            val format = extractor.getTrackFormat(i)
//            val mime = format.getString(MediaFormat.KEY_MIME) ?: ""
//            if (mime.startsWith("audio/")) {
//                audioTrackIndex = i
//                audioFormat = format
//                break
//            }
//        }
//
//        if (audioTrackIndex == -1 || audioFormat == null) {
//            return null
//        }
//
//        extractor.selectTrack(audioTrackIndex)
//
//        // Get audio properties
//        val sampleRate = audioFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE)
//        val channelCount = audioFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
//
//        // Create decoder
//        val mime = audioFormat.getString(MediaFormat.KEY_MIME) ?: return null
//        val decoder = MediaCodec.createDecoderByType(mime)
//        decoder.configure(audioFormat, null, null, 0)
//        decoder.start()
//
//        // Collect PCM data in memory
//        val pcmDataList = mutableListOf<ByteArray>()
//        val bufferInfo = MediaCodec.BufferInfo()
//        var isEOS = false
//
//        while (!isEOS) {
//            // Feed input
//            val inputBufferIndex = decoder.dequeueInputBuffer(10000)
//            if (inputBufferIndex >= 0) {
//                val inputBuffer = decoder.getInputBuffer(inputBufferIndex)
//                val sampleSize = extractor.readSampleData(inputBuffer!!, 0)
//
//                if (sampleSize < 0) {
//                    decoder.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
//                } else {
//                    val presentationTime = extractor.sampleTime
//                    decoder.queueInputBuffer(inputBufferIndex, 0, sampleSize, presentationTime, 0)
//                    extractor.advance()
//                }
//            }
//
//            // Get output
//            val outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 10000)
//            if (outputBufferIndex >= 0) {
//                val outputBuffer = decoder.getOutputBuffer(outputBufferIndex)
//
//                if (bufferInfo.size > 0) {
//                    val chunk = ByteArray(bufferInfo.size)
//                    outputBuffer?.position(bufferInfo.offset)
//                    outputBuffer?.get(chunk, 0, bufferInfo.size)
//                    pcmDataList.add(chunk)
//                }
//
//                decoder.releaseOutputBuffer(outputBufferIndex, false)
//
//                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
//                    isEOS = true
//                }
//            }
//        }
//
//        decoder.stop()
//        decoder.release()
//        extractor.release()
//
//        // Combine all PCM chunks
//        val totalSize = pcmDataList.sumOf { it.size }
//        val combinedPCM = ByteArray(totalSize)
//        var offset = 0
//        for (chunk in pcmDataList) {
//            System.arraycopy(chunk, 0, combinedPCM, offset, chunk.size)
//            offset += chunk.size
//        }
//
//        return AudioData(combinedPCM, sampleRate, channelCount)
//    } catch (e: Exception) {
//        e.printStackTrace()
//        extractor.release()
//        return null
//    }
//}
//
//@Preview
//@Composable
//fun MusicNodesScreenPreview() {
//    MusicNodesScreen(navController = rememberNavController())
//}

@Composable
fun MusicNodesScreen(naController: NavController){

}