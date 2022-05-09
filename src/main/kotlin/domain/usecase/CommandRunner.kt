package domain.usecase

class CommandRunner {
    fun run(command: String) {
        val process: Process = ProcessBuilder(command).start()
        process.inputStream.reader(Charsets.UTF_8).use {
            println(it.readText())
        }
        process.waitFor()
    }
}