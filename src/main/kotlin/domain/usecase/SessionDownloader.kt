package domain.usecase

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
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

        val client = HttpClient(CIO) {
            engine {
                requestTimeout = 0
                threadsCount = 4
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