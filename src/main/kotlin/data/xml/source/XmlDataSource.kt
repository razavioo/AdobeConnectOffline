package data.xml.source

import com.tickaroo.tikxml.TikXml
import data.xml.model.Root
import okio.BufferedSource

class XmlDataSource {
    fun readRoot(source: BufferedSource): Root? {
        val parser = TikXml.Builder()
            .exceptionOnUnreadXml(false)
            .build()
        return parser.read(source, Root::class.java)
    }
}