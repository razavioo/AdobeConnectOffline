import domain.model.Stream
import domain.model.StreamType
import domain.usecase.GetStreams
import okio.ExperimentalFileSystem
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

@ExperimentalFileSystem
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println(ARGUMENT_ISSUE_DESCRIPTION)
        return
    }

    val pathArgument: Path = args[0].toPath()
    val paths = FileSystem.SYSTEM.list(pathArgument)

    paths.forEach { path ->
        if (path.name == INDEX_STREAM_FILE_NAME) {
            val streams = GetStreams()(path)

            val command = getCommand(streams)
            for (stream in streams) {
                val mediaAbsolutePath =
                    pathArgument.toFile().absolutePath + "\\" + stream.name.split("/")[1] + ".flv"
            }
        }
    }
}

fun getCommand(streams: List<Stream>): String {
    val inputs = mutableListOf<String>()
    val filters = mutableListOf<String>()
    val audioIndexes = mutableListOf<String>()
    val maps = mutableListOf<String>()

    streams.forEachIndexed { index, stream ->
        val inputTypeChar = if (stream.type == StreamType.SCREEN_SHARE) "v" else "a"
        val nameWithExtension = stream.name.split("/")[1] + ".flv"
        inputs.add("-i $nameWithExtension")
        maps.add("-map $index:$inputTypeChar")
        if (stream.type == StreamType.CAMERA_VOIP) {
            filters.add("[$index]adelay=${stream.startTime}[a$index]")
            audioIndexes.add("[a$index]")
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
            "amix=inputs=${audioIndexes.count()}" +
            "\"" +
            " " +
            maps.joinToString(separator = " ") +
            " " +
            "output.flv"
}

const val INDEX_STREAM_FILE_NAME = "indexstream.xml"
const val ARGUMENT_ISSUE_DESCRIPTION = "Exactly 1 argument expected: path to extracted zip folder!"