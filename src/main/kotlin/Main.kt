import okio.ExperimentalFileSystem
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

@ExperimentalFileSystem
fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments at Run/Debug configuration
    println("Program arguments: ${args.joinToString()}")

    if (args.isEmpty()) {
        println("Please set path of adobe connect zip file and try again!")
        return
    }

    val path: Path = args[0].toPath()
    val readmeContent = FileSystem.SYSTEM.list(path)
    println(readmeContent)


//    FFmpeg ffmpeg = new FFmpeg("/path/to/ffmpeg");
//    FFprobe ffprobe = new FFprobe("/path/to/ffprobe");
//
//    FFmpegBuilder builder = new FFmpegBuilder()
}