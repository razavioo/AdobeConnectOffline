package domain.usecase

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

class SessionDownloader {
    @InternalCoroutinesApi
    fun downloadFile(downloadPath: String, downloadUrl: String): Flow<DownloadState> = callbackFlow {
        val fileName = downloadUrl.substringAfterLast("/").substringBefore("?")
        val file = File(downloadPath.plus("/").plus(fileName))

        // CIO ENGINE SEEMS TO HAVE ISSUE ON SOME SSL CONNECTIONS:
        // SEE https://github.com/ktorio/ktor/issues/439
        val client = HttpClient(Apache) {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.HEADERS
            }
            engine {
                threadsCount = 8
                connectTimeout = 0
            }
        }

        val httpResponse: HttpResponse = client.get(downloadUrl) {
            onDownload { bytesSentTotal, contentLength ->
                trySend(DownloadState.Progressing(bytesSentTotal, contentLength))
            }
        }
        val responseBody: ByteArray = httpResponse.body()
        file.writeBytes(responseBody)
        trySend(DownloadState.Succeed(file))

        awaitClose {
            client.cancel()
        }
    }

    sealed class DownloadState {
        data class Progressing(val bytesSentTotal: Long, val contentLength: Long) : DownloadState()
        data class Succeed(val outputFile: File) : DownloadState()
    }
}