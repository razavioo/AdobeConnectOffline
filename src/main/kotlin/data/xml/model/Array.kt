package data.xml.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Array(
    @Element(name = "Object")
    var objects: List<Object>?
)