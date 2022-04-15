package domain.usecase

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class SessionNameGrabber {
    fun grab(address: String): String? {
        return try {
            val document: Document = Jsoup.connect(address).get()
            val element: Elements = document.select(ELEMENT_TITLE)
            element.firstOrNull()?.textNodes()?.first().toString()
        } catch (ignored: Exception) {
            null
        }
    }

    companion object {
        const val ELEMENT_TITLE = "title"
    }
}