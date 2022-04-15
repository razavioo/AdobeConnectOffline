import domain.usecase.FFMpegCommandCreator
import domain.usecase.SessionDownloadLinkGenerator
import domain.usecase.SessionDownloader
import domain.usecase.SessionStreamFinder
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okio.ExperimentalFileSystem
import java.io.File
import java.util.*

@InternalCoroutinesApi
@ExperimentalFileSystem
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println(ARGUMENT_ISSUE_DESCRIPTION)
        return
    }

    val pathArgument: String = args[0]
    val sessionAddress = "http://fadonline.ir/pvusl4b3io4o/?proto=true"
    val downloadLink = SessionDownloadLinkGenerator().generate(sessionAddress)
    val streams = SessionStreamFinder().findAll(pathArgument)
    val command = FFMpegCommandCreator().create(streams)
//    val outputPath = pathArgument.toPath().toFile().absolutePath + "\\" + "output.flv"
    val outputPath = "C:\\Users\\razavioo\\Desktop"

    runBlocking {
        SessionDownloader().downloadFile(outputPath, downloadLink).collect()
    }

    Scanner(System.`in`).next()
}

private const val ARGUMENT_ISSUE_DESCRIPTION =
    "Exactly 1 argument expected: path to extracted zip folder!"