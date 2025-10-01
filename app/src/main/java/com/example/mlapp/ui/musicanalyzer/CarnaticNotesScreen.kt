package com.example.mlapp.ui.musicanalyzer

import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.TarsosDSPAudioFormat
import be.tarsos.dsp.io.UniversalAudioInputStream
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.round

@Composable
fun CarnaticNotesScreen(navController: androidx.navigation.NavController) {
    var selectedAudio by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<Uri?>(null) }
    var detectedNotes by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<List<CarnaticNote>>(emptyList()) }
    var detectedScale by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
    var isProcessing by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0f0c29),
                        Color(0xFF302b63),
                        Color(0xFF24243e)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 50.dp)
        ) {
            GlassyHeader("🎵 Carnatic Notes Analyzer")

            Spacer(modifier = Modifier.height(24.dp))

            AudioUploadButton(
                modifier = Modifier.fillMaxWidth(),
                onAudioSelected = { uri ->
                    selectedAudio = uri
                }
            )

            selectedAudio?.let { uri ->
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0x22ffffff)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "📁 ${uri.lastPathSegment ?: "Audio file"}",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                isProcessing = true
                                analyzeAudio(context, uri) { notes, scale ->
                                    detectedNotes = notes
                                    detectedScale = scale
                                    isProcessing = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00d4aa)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "🎼 Analyze & Extract Notes",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when {
                isProcessing -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(60.dp),
                                color = Color(0xFF6C63FF),
                                strokeWidth = 6.dp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Analyzing audio...",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                detectedNotes.isNotEmpty() -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(androidx.compose.foundation.rememberScrollState())
                    ) {
                        detectedScale?.let {
                            ScaleIndicator(it)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        NoteCard(
                            title = "🎶 Sargam Notation (Carnatic)",
                            content = formatNotesAsSargam(detectedNotes),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        NoteCard(
                            title = "🎹 Western Notation",
                            content = formatNotesAsWestern(detectedNotes),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        NoteCard(
                            title = "📊 Note Statistics",
                            content = "Total notes: ${detectedNotes.size}\n" +
                                    "Duration: ${String.format("%.1f", detectedNotes.sumOf { it.duration.toDouble() })}s",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Upload an audio file to get started",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

data class CarnaticNote(
    val pitch: Float,
    val sargam: String,
    val westernNote: String,
    val octave: Int,
    val duration: Float
)

data class AudioData(
    val pcmData: ByteArray,
    val sampleRate: Int,
    val channelCount: Int
)

// Carnatic (Sargam) to Western note mapping
val sargamToWestern = mapOf(
    "S" to "C",
    "R1" to "C#",
    "R2" to "D",
    "G1" to "D#",
    "G2" to "E",
    "M1" to "F",
    "M2" to "F#",
    "P" to "G",
    "D1" to "G#",
    "D2" to "A",
    "N1" to "A#",
    "N2" to "B"
)

fun analyzeAudio(
    context: Context,
    audioUri: Uri,
    onResult: (List<CarnaticNote>, String) -> Unit
) {
    Thread {
        val notes = mutableListOf<CarnaticNote>()
        try {
            val audioData = decodeAudioToPCM(context, audioUri)

            if (audioData != null) {
                val sampleRate = audioData.sampleRate
                val bufferSize = 4096
                val bufferOverlap = 2048

                val audioFormat = TarsosDSPAudioFormat(
                    sampleRate.toFloat(),
                    16,
                    audioData.channelCount,
                    true,
                    false
                )

                val audioInputStream = audioData.pcmData.inputStream()
                val universalStream = UniversalAudioInputStream(audioInputStream, audioFormat)
                val dispatcher = AudioDispatcher(universalStream, bufferSize, bufferOverlap)

                val pitches = mutableListOf<Float>()

                val pitchHandler = PitchDetectionHandler { res: PitchDetectionResult, _ ->
                    val pitch = res.pitch
                    if (pitch > 0) {
                        pitches.add(pitch)
                    }
                }

                val pitchProcessor = PitchProcessor(
                    PitchProcessor.PitchEstimationAlgorithm.YIN,
                    sampleRate.toFloat(),
                    bufferSize,
                    pitchHandler
                )

                dispatcher.addAudioProcessor(pitchProcessor)
                dispatcher.run()

                // Convert pitches to notes
                notes.addAll(convertPitchesToNotes(pitches))

                // Detect scale
                val scale = detectScale(notes)

                onResult(notes, scale)
            } else {
                onResult(emptyList(), "Unknown")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(emptyList(), "Error")
        }
    }.start()
}

fun convertPitchesToNotes(pitches: List<Float>): List<CarnaticNote> {
    val notes = mutableListOf<CarnaticNote>()
    val smoothedPitches = smoothPitches(pitches)

    var currentNote: String? = null
    var currentPitch = 0f
    var duration = 0f

    for (pitch in smoothedPitches) {
        val noteInfo = pitchToNote(pitch)

        if (noteInfo.first == currentNote) {
            duration += 0.1f // Assuming each frame is ~0.1s
        } else {
            if (currentNote != null && duration > 0.15f) { // Minimum duration threshold
                notes.add(
                    CarnaticNote(
                        pitch = currentPitch,
                        sargam = currentNote,
                        westernNote = noteInfo.second,
                        octave = noteInfo.third,
                        duration = duration
                    )
                )
            }
            currentNote = noteInfo.first
            currentPitch = pitch
            duration = 0.1f
        }
    }

    // Add last note
    if (currentNote != null && duration > 0.15f) {
        val noteInfo = pitchToNote(currentPitch)
        notes.add(
            CarnaticNote(
                pitch = currentPitch,
                sargam = currentNote,
                westernNote = noteInfo.second,
                octave = noteInfo.third,
                duration = duration
            )
        )
    }

    return notes
}

fun smoothPitches(pitches: List<Float>, windowSize: Int = 5): List<Float> {
    return pitches.windowed(windowSize, 1) { window ->
        window.average().toFloat()
    }
}

fun pitchToNote(frequency: Float): Triple<String, String, Int> {
    // A4 = 440 Hz reference
    val a4 = 440.0
    val c0 = a4 * 2.0.pow(-4.75) // C0 frequency

    val h = 12 * log2(frequency / c0)
    val octave = (h / 12).toInt()
    val n = (round(h) % 12).toInt()

    val noteNames = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    val sargamNames = listOf("S", "R1", "R2", "G1", "G2", "M1", "M2", "P", "D1", "D2", "N1", "N2")

    val westernNote = noteNames[n]
    val sargamNote = sargamNames[n]

    return Triple(sargamNote, westernNote, octave)
}

fun detectScale(notes: List<CarnaticNote>): String {
    if (notes.isEmpty()) return "Unknown"

    // Count note occurrences
    val noteCounts = notes.groupingBy { it.westernNote }.eachCount()
    val dominantNote = noteCounts.maxByOrNull { it.value }?.key ?: "C"

    // Simple heuristic: most common note is likely the tonic
    val scales = mapOf(
        "C" to "C MAJOR / SA",
        "C#" to "C# MAJOR / SA (C#)",
        "D" to "D MAJOR / SA (D)",
        "D#" to "D# MAJOR / SA (D#)",
        "E" to "E MAJOR / SA (E)",
        "F" to "F MAJOR / SA (F)",
        "F#" to "F# MAJOR / SA (F#)",
        "G" to "G MAJOR / SA (G)",
        "G#" to "G# MAJOR / SA (G#)",
        "A" to "A MAJOR / SA (A)",
        "A#" to "A# MAJOR / SA (A#)",
        "B" to "B MAJOR / SA (B)"
    )

    return scales[dominantNote] ?: "Unknown"
}

fun formatNotesAsSargam(notes: List<CarnaticNote>): String {
    return notes.joinToString(" ") { note ->
        val octaveMarker = when {
            note.octave < 4 -> "."
            note.octave > 4 -> "'"
            else -> ""
        }
        val lengthMarker = if (note.duration > 0.3f) ".." else ""
        "${note.sargam}$octaveMarker$lengthMarker"
    }
}

fun formatNotesAsWestern(notes: List<CarnaticNote>): String {
    return notes.joinToString(" ") { note ->
        "${note.westernNote}${note.octave}"
    }
}

fun decodeAudioToPCM(context: Context, uri: Uri): AudioData? {
    val extractor = MediaExtractor()
    try {
        extractor.setDataSource(context, uri, null)

        var audioTrackIndex = -1
        var audioFormat: MediaFormat? = null
        for (i in 0 until extractor.trackCount) {
            val format = extractor.getTrackFormat(i)
            val mime = format.getString(MediaFormat.KEY_MIME) ?: ""
            if (mime.startsWith("audio/")) {
                audioTrackIndex = i
                audioFormat = format
                break
            }
        }

        if (audioTrackIndex == -1 || audioFormat == null) return null

        extractor.selectTrack(audioTrackIndex)

        val sampleRate = audioFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE)
        val channelCount = audioFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT)

        val mime = audioFormat.getString(MediaFormat.KEY_MIME) ?: return null
        val decoder = MediaCodec.createDecoderByType(mime)
        decoder.configure(audioFormat, null, null, 0)
        decoder.start()

        val pcmDataList = mutableListOf<ByteArray>()
        val bufferInfo = MediaCodec.BufferInfo()
        var isEOS = false

        while (!isEOS) {
            val inputBufferIndex = decoder.dequeueInputBuffer(10000)
            if (inputBufferIndex >= 0) {
                val inputBuffer = decoder.getInputBuffer(inputBufferIndex)
                val sampleSize = extractor.readSampleData(inputBuffer!!, 0)

                if (sampleSize < 0) {
                    decoder.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                } else {
                    val presentationTime = extractor.sampleTime
                    decoder.queueInputBuffer(inputBufferIndex, 0, sampleSize, presentationTime, 0)
                    extractor.advance()
                }
            }

            val outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 10000)
            if (outputBufferIndex >= 0) {
                val outputBuffer = decoder.getOutputBuffer(outputBufferIndex)

                if (bufferInfo.size > 0) {
                    val chunk = ByteArray(bufferInfo.size)
                    outputBuffer?.position(bufferInfo.offset)
                    outputBuffer?.get(chunk, 0, bufferInfo.size)
                    pcmDataList.add(chunk)
                }

                decoder.releaseOutputBuffer(outputBufferIndex, false)

                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                    isEOS = true
                }
            }
        }

        decoder.stop()
        decoder.release()
        extractor.release()

        val totalSize = pcmDataList.sumOf { it.size }
        val combinedPCM = ByteArray(totalSize)
        var offset = 0
        for (chunk in pcmDataList) {
            System.arraycopy(chunk, 0, combinedPCM, offset, chunk.size)
            offset += chunk.size
        }

        return AudioData(combinedPCM, sampleRate, channelCount)
    } catch (e: Exception) {
        e.printStackTrace()
        extractor.release()
        return null
    }
}

@Preview
@Composable
fun CarnaticNoteScreenPreview(
){

}