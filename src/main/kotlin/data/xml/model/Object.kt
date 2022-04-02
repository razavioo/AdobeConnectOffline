package data.xml.model

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
class Object(
    @PropertyElement(name = "startTime")
    var startTime: Long,
    @PropertyElement(name = "streamId")
    var streamId: String?,
    @PropertyElement(name = "streamName")
    var streamName: String?,
    @PropertyElement(name = "streamType")
    var streamType: String?
)