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
            val streams = GetStreams().invoke(path)
            println(streams)
        }
    }

//    FFmpeg ffmpeg = new FFmpeg("/path/to/ffmpeg");
//    FFprobe ffprobe = new FFprobe("/path/to/ffprobe");
//
//    FFmpegBuilder builder = new FFmpegBuilder()
}

const val INDEX_STREAM_FILE_NAME = "indexstream.xml"
const val ARGUMENT_ISSUE_DESCRIPTION = "Please set path of adobe connect zip file and try again!"