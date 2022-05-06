package data.xml.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
class Message(
    @Attribute
    var time: Long = 0,
    @Attribute
    var type: String,
    @PropertyElement(name = "text")
    var text: String?,
    @PropertyElement(name = "Method")
    var method: String,
    @PropertyElement(name = "String")
    var string: String?,
    @Element(name = "Array")
    var array: Array?
)