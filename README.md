# Carnatic-Notes-Analyzer-App

# 🎵 Carnatic Notes Analyzer App

A Kotlin + Jetpack Compose Android app that extracts Carnatic musical notes (Sargam notation) from uploaded audio files.

## Features

- ✨ Upload audio files (MP3, WAV, M4A, etc.)
- 🎼 Automatic pitch detection using TarsosDSP
- 🎶 Convert pitches to Carnatic Sargam notation (S, R, G, M, P, D, N)
- 🎹 Display Western notation alongside Sargam
- 🎯 Automatic scale detection (e.g., G# MAJOR)
- 📊 Note statistics and duration analysis
- 🎨 Beautiful gradient UI with Material 3

## Example Output

```
DETECTED SCALE: G# MAJOR / SA (G#)

Sargam Notation (Carnatic):
G. P. P.. G. P. P.. G. P. P. m.. m.. G.. R..

Western Notation:
G3 G#3 G#3 G3 G#3 G#3 G3 G#3 G#3 F3 F3 G3 C#3
```

### 1. Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Kotlin 1.9+
- Android SDK 26 or higher
- JDK 17

### 1. Audio Upload
- User selects audio file using Android's file picker
- File is converted to URI

### 2. Audio Decoding
- Uses `MediaExtractor` and `MediaCodec` to decode audio to PCM
- Extracts sample rate and channel information

### 3. Pitch Detection
- TarsosDSP's YIN algorithm analyzes audio buffers
- Detects fundamental frequency (pitch) for each time frame
- Filters out silence and invalid pitches

### 4. Note Conversion
- Converts frequencies to musical notes using logarithmic formula
- Maps to both Western (C, D, E...) and Carnatic (S, R, G...) notation
- Handles octave detection (lower, middle, upper)

### 5. Note Formatting
- Groups consecutive same notes
- Adds octave markers: `.` (lower), `'` (upper)
- Adds duration markers: `..` (long notes)

## Carnatic Notation Guide

### Sargam Notes
- **S** (Sa) = C
- **R1** (Shuddha Rishabham) = C#
- **R2** (Chatusruti Rishabham) = D
- **G1** (Shuddha Gandharam) = D#
- **G2** (Antara Gandharam) = E
- **M1** (Shuddha Madhyamam) = F
- **M2** (Prati Madhyamam) = F#
- **P** (Panchamam) = G
- **D1** (Shuddha Dhaivatam) = G#
- **D2** (Chatusruti Dhaivatam) = A
- **N1** (Shuddha Nishadam) = A#
- **N2** (Kaisiki Nishadam) = B

### Octave Markers
- `.` (dot below) = Lower octave
- No marker = Middle octave
- `'` (apostrophe) = Upper octave

### Duration Markers
- Single note = Short duration
- `..` = Long duration (held note)

## Troubleshooting

### Issue: All zeros in output
**Solution**: Ensure audio file has clear musical content. Try files with vocals or melodic instruments.

### Issue: ClassNotFoundException for javax.sound
**Solution**: Make sure you're using the Android-compatible version with MediaCodec, not AudioDispatcherFactory.fromFile()

### Issue: No permission to read files
**Solution**: Check that READ_MEDIA_AUDIO permission is granted in app settings

### Issue: App crashes on audio processing
**Solution**: Process audio on background thread (already implemented with Thread {})

## Limitations

- Works best with monophonic audio (single melody line)
- Polyphonic music (chords) may produce mixed results
- Background noise can affect pitch detection accuracy
- Very fast passages may not be captured accurately

## Future Enhancements

- [ ] Add lyrics timing sync
- [ ] Export notes to PDF/Text file
- [ ] Real-time recording and analysis
- [ ] Support for gamakas (ornamentations)
- [ ] Tempo detection
- [ ] Beat tracking
- [ ] Multiple ragas identification

## License

This project uses TarsosDSP which is licensed under GPL-3.0.

## Credits

- **TarsosDSP** - Audio processing library
- **YIN Algorithm** - Pitch detection algorithm
- **Jetpack Compose** - Modern Android UI toolkit

---

Made with ❤️ for Carnatic music enthusiasts!