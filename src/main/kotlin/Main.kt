import domain.usecase.SessionDownloadLinkGenerator
import domain.usecase.SessionStreamFinder
import okio.ExperimentalFileSystem
import okio.Path.Companion.toPath

@ExperimentalFileSystem
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println(ARGUMENT_ISSUE_DESCRIPTION)
        return
    }

    val pathArgument: String = args[0]
    val address = "http://fadonline.ir/p981febg5t5g/?proto=true"
    val downloadLink = SessionDownloadLinkGenerator().generate(address)
    val streams = SessionStreamFinder().findAll(pathArgument)
    val command = FFMpegCommandCreator().create(streams)
    val outputPath = pathArgument.toPath().toFile().absolutePath + "\\" + "output.flv"
}

private const val ARGUMENT_ISSUE_DESCRIPTION =
    "Exactly 1 argument expected: path to extracted zip folder!"