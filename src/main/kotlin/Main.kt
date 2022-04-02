import domain.model.StreamType
import domain.usecase.GetStreams
import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import net.bramp.ffmpeg.probe.FFmpegProbeResult
import net.bramp.ffmpeg.probe.FFmpegStream
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

    val pathFFmpeg: Path = args[1].toPath()
    val ffmpeg = FFmpeg(pathFFmpeg.toFile().absolutePath + "\\ffmpeg.exe")
    val ffprobe = FFprobe(pathFFmpeg.toFile().absolutePath + "\\ffprobe.exe")

    val results: MutableList<FFmpegProbeResult> = ArrayList()
    paths.forEach { path ->
        if (path.name == INDEX_STREAM_FILE_NAME) {
            val streams = GetStreams()(path)

            for (stream in streams) {
                val mediaPath = pathArgument.toFile().absolutePath + "\\" + stream.name.split("/")[1] + ".flv"
                val result = ffprobe.probe(mediaPath)
                result.streams.first().apply {
                    codec_type = if (stream.type == StreamType.SCREEN_SHARE) {
                        FFmpegStream.CodecType.VIDEO
                    } else {
                        FFmpegStream.CodecType.AUDIO
                    }
                }

                result.format.apply {
                    start_time = stream.startTime.toDouble()
                }

                results.add(result)
            }
        }
    }

    val builder = FFmpegBuilder()

    for (result in results) {
        builder.addInput(result)
    }

    builder
        .overrideOutputFiles(true) // Override the output if it exists
        .addOutput("C:\\Users\\razavioo\\Desktop\\DevOps\\session01\\output.mp4") // Filename for the destination
        .setFormat("flv") // Format is inferred from filename, or can be set
        .disableSubtitle() // No subtiles
        .setAudioChannels(1) // Mono audio
        .setAudioCodec("aac") // using the aac codec
        .setAudioSampleRate(48000) // at 48KHz
        .setAudioBitRate(32768) // at 32 kbit/s
        .setVideoCodec("libx264") // Video using x264
        .setVideoFrameRate(10, 1) // at 24 frames per second
        .setVideoResolution(160, 120) // at 640x480 resolution
        .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
        .done()

    builder.setVerbosity(FFmpegBuilder.Verbosity.DEBUG)

    val executor = FFmpegExecutor(ffmpeg, ffprobe)
    executor.createJob(builder).run()
}

const val INDEX_STREAM_FILE_NAME = "indexstream.xml"
const val ARGUMENT_ISSUE_DESCRIPTION = "Please set path of adobe connect zip file and try again!"