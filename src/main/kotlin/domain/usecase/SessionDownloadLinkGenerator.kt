package domain.usecase

import java.io.UnsupportedEncodingException
import java.net.URI
import java.net.URL
import java.net.URLDecoder

class SessionDownloadLinkGenerator {
    fun generate(address: String): String {
        val fileName = generateFileName(address)
        val uri = URI(address)
        val baseAddress = uri.scheme + "://" + uri.authority + uri.path

        var sessionPart = ""
        val queryParams = splitQuery(uri.toURL())
        for (pair in queryParams.entries) {
            if (pair.key == "session") {
                sessionPart = "${pair.key}=${pair.value}&"
                break
            }
        }

        return baseAddress.plus("output/${fileName}.zip?${sessionPart}download=zip")
    }

    private fun generateFileName(address: String): String {
        var fileName = SessionNameGrabber().grab(address)
        if (fileName == null) {
            fileName = "unknown-file-name-" + System.currentTimeMillis()
        }

        return fileName
            .replace("/", "-")
            .replace("\\s".toRegex(), "-")
    }

    @Throws(UnsupportedEncodingException::class)
    fun splitQuery(url: URL): Map<String, String> {
        val queryPairs: MutableMap<String, String> = LinkedHashMap()
        val query = url.query
        val pairs = query.split("&").toTypedArray()
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            queryPairs[URLDecoder.decode(pair.substring(0, idx), "UTF-8")] =
                URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
        }
        return queryPairs
    }
}