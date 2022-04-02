package data.file.source

import okio.*

class FileDataSource {
    @ExperimentalFileSystem
    fun getBufferedSource(path: Path): BufferedSource {
        return FileSystem.SYSTEM.source(path).buffer()
    }
}