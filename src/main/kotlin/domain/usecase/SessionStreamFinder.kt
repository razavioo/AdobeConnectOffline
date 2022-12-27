package domain.usecase

import domain.model.Stream
import okio.ExperimentalFileSystem
import okio.FileSystem
import okio.Path.Companion.toPath

@ExperimentalFileSystem
class SessionStreamFinder {
    fun findAll(sessionPath: String): List<Stream> {
        val paths = FileSystem.SYSTEM.list(sessionPath.toPath())
        paths.forEach { path ->
            val validFiles = listOf(INDEX_STREAM_FILE_NAME, MAIN_STREAM_FILE_NAME)
            if (validFiles.contains(path.name)) {
                return GetStreams()(path).map {
                    it.copy(name = sessionPath + "/" + it.name.split("/")[1] + ".flv")
                }
            }
        }
        return arrayListOf()
    }

    companion object {
        private const val INDEX_STREAM_FILE_NAME = "indexstream.xml"
        private const val MAIN_STREAM_FILE_NAME = "mainstream.xml"
    }
}