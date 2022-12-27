import domain.usecase.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import okio.ExperimentalFileSystem
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import utils.ZipUtils
import java.io.File
import java.util.*

@Command(name = "Main", version = ["Main 1.0"], mixinStandardHelpOptions = true)
class MainCommand : Runnable {
    @Option(names = ["-dl", "--download-link"], description = ["Download Link"])
    var downloadLink: String? = null

    @Option(names = ["-z", "--zip-file-path"], description = ["Zip file path"])
    var zipFile: File? = null

    @Option(names = ["-o", "--output-path"], description = ["Output Path"], required = true)
    lateinit var outputPath: String

    @OptIn(InternalCoroutinesApi::class)
    override fun run() {
        downloadLink?.let { downloadLink ->
            val sessionDownloadLink = SessionDownloadLinkGenerator().generate(downloadLink)
            println("Session download link created: $sessionDownloadLink")

            runBlocking {
                var percentage = -1
                SessionDownloader().downloadFile(outputPath, sessionDownloadLink).collectLatest { state ->
                    when (state) {
                        is SessionDownloader.DownloadState.Progressing -> {
                            val newPercentage = ((state.bytesSentTotal.toDouble() / state.contentLength) * 100).toInt()
                            if (newPercentage != percentage) {
                                percentage = newPercentage
                                println("Received: $percentage percent!")
                            }
                        }
                        is SessionDownloader.DownloadState.Succeed -> {
                            val zipFile = state.outputFile
                            println("Zip file downloaded: ${zipFile.absolutePath}!")

                            val unzipParentDirectory = unzip(zipFile)
                            runFFMpegCommand(unzipParentDirectory)
                        }
                    }
                }
            }
        } ?: run {
            zipFile?.let { zipFile ->
                val unzipParentDirectory = unzip(zipFile)
                runFFMpegCommand(unzipParentDirectory)
            } ?: run {
                println("Download link or zip file should be added!")
            }
        }
    }

    private fun unzip(zipFile: File): String {
        val unzipParentDirectory = zipFile.absolutePath.substringBeforeLast(".zip")
        ZipUtils.unzip(zipFile, unzipParentDirectory)
        println("Zip file unzipped: ${zipFile.absolutePath}!")
        return unzipParentDirectory
    }

    @OptIn(ExperimentalFileSystem::class)
    private fun runFFMpegCommand(inputPath: String) {
        val streams = SessionStreamFinder().findAll(inputPath)
        val command = FFMpegCommandCreator().create(streams, outputPath)
        println("Command is: $command")
        Runtime.getRuntime().exec(command)
        Scanner(System.`in`).next()

        CommandRunner().run(command)
    }
}