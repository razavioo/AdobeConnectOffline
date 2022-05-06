package data.xml.mapper

import data.xml.model.Root
import domain.model.Stream
import domain.model.StreamType

class RootStreamListMapper {
    fun mapFrom(root: Root): List<Stream> {
        val items: MutableList<Stream> = ArrayList()
        root.messages.forEach { message ->
            message.array?.objects?.let { objects ->
                val startTime: Long = objects[0].startTime
                val streamId: String = objects[0].streamId.toString()
                val streamName: String = objects[0].streamName.toString()
                val streamType: StreamType = when (objects[0].streamType) {
                    SCREEN_SHARE_STRING_FORMAT -> StreamType.SCREEN_SHARE
                    CAMERA_VOIP_STRING_FORMAT -> StreamType.CAMERA_VOIP
                    else -> StreamType.UNKNOWN
                }

                when (message.string.toString()) {
                    STREAM_ADDED_MESSAGE -> {
                        val stream = Stream(streamId, streamName, streamType, startTime)
                        items.add(stream)
                    }
                    STREAM_REMOVED_MESSAGE -> {
                        items.firstOrNull {
                            it.id == streamId
                        }.apply {
                            this?.stopTime = message.time
                        }
                    }
                }
            }
        }

        return items
    }

    companion object {
        const val SCREEN_SHARE_STRING_FORMAT: String = "screenshare"
        const val CAMERA_VOIP_STRING_FORMAT: String = "cameraVoip"
        const val STREAM_ADDED_MESSAGE: String = "streamAdded"
        const val STREAM_REMOVED_MESSAGE: String = "streamRemoved"
    }
}