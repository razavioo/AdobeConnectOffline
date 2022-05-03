package domain.usecase

import domain.model.Stream
import domain.model.StreamType

class FFMpegCommandCreator {
    fun create(streams: List<Stream>): String {
        val inputs = mutableListOf<String>()
        val audioFilters = mutableListOf<String>()
        val videoFilters = mutableListOf<String>()
        val audioIndexes = mutableListOf<String>()
        val videoIndexes = mutableListOf<String>()

        streams.forEachIndexed { index, stream ->
            val nameWithExtension = stream.name.split("/")[1] + ".flv"
            inputs.add("-i $nameWithExtension")
            if (stream.type == StreamType.CAMERA_VOIP) {
                audioFilters.add("[$index]adelay=${stream.startTime}ms[audio$index]")
                audioIndexes.add("[audio$index]")
            } else {
                videoFilters.add("[$index]tpad=start_duration=${stream.startTime}ms:start_mode=add:color=black[video$index]")
                videoIndexes.add("[video$index]")
            }
        }

        return "ffmpeg" +
                " " +
                inputs.joinToString(" ") +
                " " +
                "-filter_complex" +
                " " +
                "\"" +
                audioFilters.joinToString(
                    separator = ";",
                    postfix = ";"
                ) +
                videoFilters.joinToString(
                    separator = ";",
                    postfix = ";"
                ) +
                audioIndexes.joinToString(separator = "") +
                "amix=inputs=${audioIndexes.count()}[audios]" +
                ";" +
                videoIndexes.joinToString(separator = "") +
                "mix=inputs=${videoIndexes.count()}[videos]" +
                "\"" +
                " " +
                "-map [audios] " +
                "-map [videos] " +
                " " +
                "output.flv -y"
    }
}