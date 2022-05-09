package domain.usecase

import domain.model.Stream
import domain.model.StreamType

class FFMpegCommandCreator {
    fun create(streams: List<Stream>, outputPath: String): String {
        val inputs = mutableListOf<String>()
        val audioIndexes = mutableListOf<String>()
        val videoIndexes = mutableListOf<String>()

        streams.forEachIndexed { index, stream ->
            inputs.add("-itsoffset ${stream.startTime}ms -i ${stream.name}")
            if (stream.type == StreamType.CAMERA_VOIP) {
                audioIndexes.add("[$index:a]")
            } else {
                videoIndexes.add("[$index:v]")
            }
        }

        return "ffmpeg " + inputs.joinToString(" ") + " " +
                "-filter_complex \"" +
                audioIndexes.joinToString(separator = "") + " concat=n=${audioIndexes.size}:v=0:a=1 [audios];" +
                videoIndexes.joinToString(separator = "") + " concat=n=${videoIndexes.size}:v=1:a=0 [videos]" +
                "\"" +
                " " +
                "-map [audios] -map [videos] " +
                "-y ${outputPath}\\output.flv"
    }
}