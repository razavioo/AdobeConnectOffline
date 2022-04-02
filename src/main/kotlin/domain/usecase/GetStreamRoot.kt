package domain.usecase

import data.file.source.FileDataSource
import data.xml.model.Root
import data.xml.source.XmlDataSource
import okio.BufferedSource
import okio.ExperimentalFileSystem
import okio.Path

@ExperimentalFileSystem
class GetStreamRoot {
    private val fileDataSource: FileDataSource = FileDataSource()
    private val xmlDataSource: XmlDataSource = XmlDataSource()

    operator fun invoke(path: Path): Root? {
        val data: BufferedSource = fileDataSource.getBufferedSource(path)
        return xmlDataSource.readRoot(data)
    }
}