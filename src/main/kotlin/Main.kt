import kotlinx.coroutines.InternalCoroutinesApi
import okio.ExperimentalFileSystem
import picocli.CommandLine
import kotlin.system.exitProcess

@InternalCoroutinesApi
@ExperimentalFileSystem
fun main(vararg args: String) {
    val exitCode: Int = CommandLine(MainCommand()).execute(*args)
    exitProcess(exitCode)
}