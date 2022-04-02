package domain.usecase

import data.xml.mapper.RootStreamListMapper
import domain.model.Stream
import okio.ExperimentalFileSystem
import okio.Path

@ExperimentalFileSystem
class GetStreams {
    private val getStreamRoot: GetStreamRoot = GetStreamRoot()
    private val rootStreamListMapper: RootStreamListMapper = RootStreamListMapper()

    operator fun invoke(path: Path): List<Stream> {
        return getStreamRoot(path)?.let {
            rootStreamListMapper.mapFrom(it)
        } ?: run {
            emptyList()
        }
    }
}