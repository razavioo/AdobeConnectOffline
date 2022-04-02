package data.xml.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Root(
    @PropertyElement(name = "DataPos")
    var dataPosition: Int,
    @Element(name = "Message")
    var messages: List<Message>
)