package domain.usecase

class SessionDownloadLinkGenerator {
    fun generate(address: String): String {
        val fileName = generateFileName(address)
        val baseAddress = generateBaseURL(address)
        return baseAddress.plus("output/${fileName}.zip?download=zip")
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

    // TODO: Need more URLs to cover!
    private fun generateBaseURL(address: String): String {
        var baseAddress = ""
        if (address.endsWith("?proto=true")) {
            baseAddress = address.split("?proto=true")[0]
        }
        return baseAddress
    }
}