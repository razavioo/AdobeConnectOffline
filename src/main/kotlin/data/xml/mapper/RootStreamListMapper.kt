package data.xml.mapper

import data.xml.model.Root
import domain.model.Stream
import domain.model.StreamType

class RootStreamListMapper {
    fun mapFrom(root: Root): List<Stream> {
        val items: MutableList<Stream> = ArrayList()

        root.message?.forEach {
            if (it.string.toString() == "streamAdded") {
                it.array?.objects?.let { objects ->
                    val startTime: Long = objects[0].startTime
                    val streamId: String = objects[0].streamId.toString()
                    val streamName: String = objects[0].streamName.toString()
                    val streamType: StreamType = when (objects[0].streamType) {
                        SCREEN_SHARE_STRING_FORMAT -> StreamType.SCREEN_SHARE
                        CAMERA_VOIP_STRING_FORMAT -> StreamType.CAMERA_VOIP
                        else -> StreamType.UNKNOWN
                    }
                    val stream = Stream(streamId, streamName, streamType, startTime)
                    items.add(stream)
                }
            }
        }

        return items
    }

    companion object {
        const val SCREEN_SHARE_STRING_FORMAT: String = "screenshare"
        const val CAMERA_VOIP_STRING_FORMAT: String = "cameraVoip"
    }
}