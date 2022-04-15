import domain.model.Stream
import domain.model.StreamType

class FFMpegCommandCreator {
    fun create(streams: List<Stream>): String {
        val videoTime = streams.first { it.type == StreamType.SCREEN_SHARE }.startTime

        val inputs = mutableListOf<String>()
        val filters = mutableListOf<String>()
        val audioIndexes = mutableListOf<String>()
        val maps = mutableListOf<String>()

        streams.filter { it.startTime - videoTime >= 0 }.forEachIndexed { index, stream ->
            val inputTypeChar = if (stream.type == StreamType.SCREEN_SHARE) "v" else "a"
            val nameWithExtension = stream.name.split("/")[1] + ".flv"
            inputs.add("-i $nameWithExtension")
            if (stream.type == StreamType.CAMERA_VOIP) {
                filters.add("[$index]adelay=${stream.startTime - videoTime}[audio$index]")
                audioIndexes.add("[audio$index]")
            } else {
                maps.add("-map $index:$inputTypeChar")
            }
        }

        return "ffmpeg" +
                " " +
                inputs.joinToString(" ") +
                " " +
                "-filter_complex" +
                " " +
                "\"" +
                filters.joinToString(
                    separator = ";",
                    postfix = ";"
                ) +
                audioIndexes.joinToString(separator = "") +
                "amix=inputs=${audioIndexes.count()}[audios]" +
                "\"" +
                " " +
                "-map [audios] " +
                maps.joinToString(separator = " ") +
                " " +
                "output.flv"
    }
}