import domain.usecase.FFMpegCommandCreator
import domain.usecase.SessionDownloadLinkGenerator
import domain.usecase.SessionDownloader
import domain.usecase.SessionStreamFinder
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import okio.ExperimentalFileSystem
import utils.ZipUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*


@InternalCoroutinesApi
@ExperimentalFileSystem
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println(ARGUMENT_ISSUE_DESCRIPTION)
        return
    }

    val downloadFolderPath = "C:\\Users\\razavioo\\Desktop\\DevOps\\material"

    val sessionLink: String = args[0]
    val sessionDownloadLink = SessionDownloadLinkGenerator().generate(sessionLink)

    runBlocking {
        SessionDownloader().downloadFile(downloadFolderPath, sessionDownloadLink).collectLatest { state ->
            when (state) {
                is SessionDownloader.DownloadState.Progressing -> {
                    val percentage = ((state.bytesSentTotal.toDouble() / state.contentLength) * 100).toInt()
                    println("Received: $percentage percent!")
                }
                is SessionDownloader.DownloadState.Succeed -> {
                    val zipFile = state.outputFile
                    println("Zip file downloaded: ${zipFile.absolutePath}!")

                    val unzipParentDirectory = zipFile.absolutePath.substringBeforeLast(".zip")
                    ZipUtils.unzip(zipFile, unzipParentDirectory)

                    val streams = SessionStreamFinder().findAll(unzipParentDirectory)
                    val command = FFMpegCommandCreator().create(streams)
                    println("Command is: $command")

//                    runCommand(command)
                }
            }
        }
    }

    Scanner(System.`in`).next()
}

fun runCommand(command: String) {
    val proc = Runtime.getRuntime().exec(command)
    val reader = BufferedReader(InputStreamReader(proc.inputStream))

    var line: String
    while (reader.readLine().also { line = it } != null) {
        print(line.trimIndent())
    }

    proc.waitFor()
}

private const val ARGUMENT_ISSUE_DESCRIPTION =
    "Exactly 1 argument expected: Adobe connect URL!"